package dorand.com.gpsc.ui.fragments.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import dorand.com.gpsc.service.intf.IGPSummaryEntry;
import dorand.com.gpsc.service.intf.IGPSummaryListDelegate;
import com.dorand.gpsc.ui.R;

public class SummaryListAdapter extends ArrayAdapter<Pair<String, String>> {

	private Activity activity;
	private IGPSummaryListDelegate delegate;

	public SummaryListAdapter(Activity context, int resource,
			IGPSummaryListDelegate _delegate) {
		super(context, resource);
		activity = context;
		delegate = _delegate;
	}

	@Override
	public int getCount() {
		// Last element is (currently) garbage...
		int ret = delegate.getSummaryList().size();
		return ret == 0 ? ret : ret - 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = makeView(convertView, parent);

		if (view != null) {

			if (position % 2 == 0) {
				view.setBackgroundColor(Color.WHITE);
			} else {
				view.setBackgroundColor(Color.LTGRAY);
			}

			IGPSummaryEntry entry = delegate.getSummaryList().get(position);

			TextView nameView = (TextView) view.findViewById(R.id.summaryName);
			nameView.setText(entry.getName());

			TextView valueView = (TextView) view
					.findViewById(R.id.summaryValue);
			valueView.setText(entry.getValue());
		}
		return view;
	}

	private View makeView(View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			view = inflater.inflate(R.layout.summary_cell, parent, false);

			int width = view.getWidth();
			TextView nameView = (TextView) view.findViewById(R.id.summaryName);
			nameView.setWidth(Double.valueOf(width * 0.6).intValue());

			TextView valueView = (TextView) view
					.findViewById(R.id.summaryValue);
			valueView.setWidth(Double.valueOf(width * 0.4).intValue());
		} else {
			view = convertView;
		}
		return view;
	}

}
