package dorand.com.gpsc.service.http.impl;

import com.ibm.json.java.JSONObject;

import java.util.Calendar;

import dorand.com.gpsc.service.http.intf.IGPCachedResponse;

public class GPCachedResponse implements IGPCachedResponse {

	private static final int FIVE_MIN = 1000 * 60 * 5;
	private final long mExpires;
	private final JSONObject mJSONObject;

	public GPCachedResponse(JSONObject jo) {
		mJSONObject = jo;
		mExpires = Calendar.getInstance().getTimeInMillis() + FIVE_MIN;
	}

	@Override
	public JSONObject getResponse() {
		return mJSONObject;
	}

	@Override
	public boolean isExpired() {
		return Calendar.getInstance().getTimeInMillis() > mExpires;
	}

}
