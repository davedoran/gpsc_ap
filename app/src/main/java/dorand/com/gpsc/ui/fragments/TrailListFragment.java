package dorand.com.gpsc.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dorand.gpsc.ui.R;

import java.util.ArrayList;
import java.util.List;

import dorand.com.gpsc.service.http.intf.IGPTrailConditionsResponse;
import dorand.com.gpsc.service.http.intf.IGPTrailResponseHandler;
import dorand.com.gpsc.service.impl.GPTrailConditionsResponse;
import dorand.com.gpsc.service.intf.IGPError;
import dorand.com.gpsc.service.intf.IGPTrailListDelegate;
import dorand.com.gpsc.service.intf.IGPTrailStatus;
import dorand.com.gpsc.ui.GPToaster;
import dorand.com.gpsc.ui.fragments.adapters.TrailListAdapter;

public abstract class TrailListFragment extends ListFragment implements IGPTrailListDelegate {

	private List<IGPTrailStatus> mTrailStatus;
	private TrailListAdapter mTrailListAdapter;

	private IGPTrailResponseHandler mResponseHandler = new IGPTrailResponseHandler() {

		@Override
		public void onResponse(IGPTrailConditionsResponse response) {
			mTrailStatus.clear();
			mTrailStatus.addAll(response.getTrailStatus());
			Activity activity = getActivity();
			if (activity != null) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mTrailListAdapter != null) {
							mTrailListAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		}

		@Override
		public void onError(IGPError error) {
			mTrailStatus.clear();
			mTrailStatus.add(new IGPTrailStatus() {
				
				@Override
				public EGPTrailStatus getStatus() {
					return EGPTrailStatus.CLEAR;
				}
				
				@Override
				public String getName() {
					return getActivity().getString(R.string.server_unreachable);
				}
				
				@Override
				public String getDistance() {
					return null;
				}
				
				@Override
				public String getDate() {
					return null;
				}
			});
			
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mTrailListAdapter.notifyDataSetChanged();
				}
			});
		}

		@Override
		public void onCachedResponse(GPTrailConditionsResponse response) {
			onResponse(response);
			GPToaster.toast(getActivity(), R.string.stale_data, Toast.LENGTH_LONG);
		}

	};

	protected TrailListFragment() {
		mTrailStatus = new ArrayList<IGPTrailStatus>();
	}

	@Override
	public List<IGPTrailStatus> getTrailList() {
		return mTrailStatus;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.trail_list, container, false);
		mTrailListAdapter = makeListAdapter();
		setListAdapter(mTrailListAdapter);
		requestContent();
		return rootView;
	}

	protected final IGPTrailResponseHandler getResponseHandler() {
		return mResponseHandler;
	}

	protected abstract TrailListAdapter makeListAdapter();

	protected abstract void requestContent();
}
