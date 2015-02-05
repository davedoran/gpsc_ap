package dorand.com.gpsc.service.impl;

import dorand.com.gpsc.service.intf.IGPSummaryEntry;

public class GPSummaryEntry implements IGPSummaryEntry {

	private String mName;
	private String mValue;

	public GPSummaryEntry(String _name, String _val) {
		mName = _name;
		mValue = _val;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getValue() {
		return mValue;
	}
}
