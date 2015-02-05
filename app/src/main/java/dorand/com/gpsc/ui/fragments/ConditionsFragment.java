package dorand.com.gpsc.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dorand.gpsc.ui.R;

import java.util.ArrayList;
import java.util.List;

import dorand.com.gpsc.service.http.impl.GPHttpService;
import dorand.com.gpsc.service.http.intf.IGPSummaryResponse;
import dorand.com.gpsc.service.http.intf.IGPSummaryResponseHandler;
import dorand.com.gpsc.service.intf.IGPError;
import dorand.com.gpsc.service.intf.IGPSummaryEntry;
import dorand.com.gpsc.service.intf.IGPSummaryListDelegate;
import dorand.com.gpsc.ui.GPToaster;
import dorand.com.gpsc.ui.fragments.adapters.SummaryListAdapter;

public class ConditionsFragment extends ListFragment implements IGPSummaryListDelegate {

	private SummaryListAdapter mListAdapter;
	private List<IGPSummaryEntry> summaryInfo;

	private IGPSummaryResponseHandler mResponseHandler = new IGPSummaryResponseHandler() {

		@Override
		public void onResponse(IGPSummaryResponse response) {
			synchronized (summaryInfo) {
				summaryInfo.clear();
				summaryInfo.addAll(response.getSummaryInfo());
			}
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mListAdapter.notifyDataSetChanged();
				}
			});
		}
		
		@Override
		public void onCachedResponse(IGPSummaryResponse response) {
			onResponse(response);
			GPToaster.toast(getActivity(), R.string.stale_data, Toast.LENGTH_LONG);
		}

		@Override
		public void onError(IGPError gpError) {
			summaryInfo.clear();
			summaryInfo.add(new IGPSummaryEntry() {

				@Override
				public String getName() {
					return getString(R.string.server_unreachable);
				}

				@Override
				public String getValue() {
					return getString(R.string.na);
				}

			});

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mListAdapter.notifyDataSetChanged();
				}
			});
		}
	};

	public ConditionsFragment() {
		summaryInfo = new ArrayList<IGPSummaryEntry>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.trail_list, container, false);
		mListAdapter = new SummaryListAdapter(getActivity(), R.layout.summary_cell, this);
		setListAdapter(mListAdapter);
		GPHttpService.getSummaryConditions(mResponseHandler);
		return rootView;
	}

	@Override
	public List<IGPSummaryEntry> getSummaryList() {
		synchronized (summaryInfo) {
			return summaryInfo;
		}
	}

}
