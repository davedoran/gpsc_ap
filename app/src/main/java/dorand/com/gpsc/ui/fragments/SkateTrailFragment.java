package dorand.com.gpsc.ui.fragments;

import com.dorand.gpsc.ui.R;

import dorand.com.gpsc.service.http.impl.GPHttpService;
import dorand.com.gpsc.service.intf.IGPTrailListDelegate;
import dorand.com.gpsc.ui.fragments.adapters.TrailListAdapter;
import dorand.com.gpsc.ui.fragments.adapters.TrailListAdapter.TrailType;

public class SkateTrailFragment extends TrailListFragment implements IGPTrailListDelegate {

	@Override
	protected TrailListAdapter makeListAdapter() {
		return new TrailListAdapter(getActivity(), R.layout.trail_cell, this, TrailType.SKATE);
	}

	@Override
	protected void requestContent() {
		GPHttpService.getSkateSkiConditions(getResponseHandler());
	}
}
