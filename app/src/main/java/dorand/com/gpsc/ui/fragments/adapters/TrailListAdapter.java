package dorand.com.gpsc.ui.fragments.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dorand.gpsc.ui.R;

import java.util.List;

import dorand.com.gpsc.service.intf.IGPTrailListDelegate;
import dorand.com.gpsc.service.intf.IGPTrailStatus;
import dorand.com.gpsc.service.intf.IGPTrailStatus.EGPTrailStatus;

public class TrailListAdapter extends ArrayAdapter<IGPTrailStatus> {

	private static final String OPEN_TRAIL = "#5500FF00";
	private static final String CLOSED_TRAIL = "#55FF0000";
	private static final String WARN_TRAIL = "#55FFFF00";

	private Activity activity;

	private IGPTrailListDelegate delegate;

	public enum TrailType {
		CLASSIC, SKATE, BACKCOUNTRY
	}

	private TrailType type;

	public TrailListAdapter(Activity context, int resource,
			IGPTrailListDelegate _delegate, TrailType _type) {
		super(context, resource);
		activity = context;
		delegate = _delegate;
		type = _type;
	}

	@Override
	public int getCount() {
		return delegate.getTrailList().size();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = makeView(convertView, parent);
		if (view != null) {
			List<IGPTrailStatus> trailList = delegate.getTrailList();
			if (trailList != null && trailList.size() > position) {
				IGPTrailStatus status = trailList.get(position);
				if (status != null) {
					setBackgroundColor(view, status);
					setTrailName(view, status);
					setDateAndDistance(view, status);
				}
			}
		}
		return view;
	}

	private View makeView(View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			view = inflater.inflate(R.layout.trail_cell, parent, false);
		} else {
			view = convertView;
		}
		return view;
	}

	private void setTrailName(View view, IGPTrailStatus status) {
		TextView name = (TextView) view.findViewById(R.id.trail_name);
		name.setText(status.getName());
	}

	private int setBackgroundColor(View view, IGPTrailStatus status) {
		int color = -1;
		switch (status.getStatus()) {
		case GREEN:
			color = Color.parseColor(OPEN_TRAIL);
			break;
		case YELLOW:
			color = Color.parseColor(WARN_TRAIL);
			break;
		case RED:
			color = Color.parseColor(CLOSED_TRAIL);
			break;
		case CLEAR:
			color = Color.LTGRAY;
			break;
		}
		view.setBackgroundColor(color);
		return color;
	}

	private void setDateAndDistance(View view, IGPTrailStatus status) {
		if (status.getStatus() == EGPTrailStatus.CLEAR) {
			TextView groomed = (TextView) view.findViewById(R.id.trail_date);
			groomed.setText("");

			TextView distance = (TextView) view
					.findViewById(R.id.trail_distance);
			distance.setText("");
		} else {

			String date = status.getDate();
			if (date == null) {
				date = activity.getString(R.string.na);
			}

			String dateText = null;
			switch (type) {
			case CLASSIC:
				dateText = activity.getString(R.string.trackset);
				break;
			case SKATE:
				dateText = activity.getString(R.string.groomed);
				break;
			case BACKCOUNTRY:
				dateText = activity.getString(R.string.patrolled);
				break;
			}

			TextView groomed = (TextView) view.findViewById(R.id.trail_date);
			groomed.setText(String.format("%s %s", dateText, date));

			TextView distance = (TextView) view
					.findViewById(R.id.trail_distance);
			distance.setText(String.format("%skm", status.getDistance()));
		}
	}
}
