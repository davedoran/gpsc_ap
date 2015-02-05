package dorand.com.gpsc.service.impl;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import dorand.com.gpsc.service.intf.IGPTrailStatus;
import dorand.com.gpsc.service.util.GPJSON;

public class GPTrailStatus implements IGPTrailStatus {

	private String mName;
	private String mGroomed;
	private String mDistance;
	private EGPTrailStatus mStatus;

	// Factory method
	public static IGPTrailStatus fromJSONArray(JSONArray contentArray) {
		IGPTrailStatus ret = null;
		if (contentArray != null && contentArray.size() > 2) {
			EGPTrailStatus status = parseStatus(contentArray);
			String name = parseName(contentArray);
			String groomed = null;
			String distance = null;
			try {
				if (contentArray.size() == 3) {
					groomed = null;
					distance = ((Number) ((JSONObject) contentArray.get(2)).get(GPJSON.V)).toString();
				} else if (contentArray.size() == 4) {
					groomed = (String) ((JSONObject) contentArray.get(2)).get(GPJSON.F);
					distance = ((Number) ((JSONObject) contentArray.get(3)).get(GPJSON.V)).toString();
				}
			} catch (Exception e) {
				groomed = null;
				distance = "Unknown...";
			}
			ret = new GPTrailStatus(name, groomed, distance, status);
		}
		return ret;
	}

	private GPTrailStatus(String _name, String _groomed, String _distance, EGPTrailStatus _status) {
		mName = _name;
		mGroomed = _groomed;
		mDistance = _distance;
		mStatus = _status;
	}

	private static String parseName(JSONArray contentArray) {
		String name = null;
		JSONObject jo = (JSONObject) contentArray.get(1);
		if (jo != null && jo.containsKey(GPJSON.V)) {
			name = (String) jo.get(GPJSON.V);
		} else {
			name = "Unknown...";
		}
		return name;
	}

	private static EGPTrailStatus parseStatus(JSONArray contentArray) {
		EGPTrailStatus status = EGPTrailStatus.CLEAR;
		JSONObject jo = (JSONObject) contentArray.get(0);
		if (jo != null && jo.containsKey(GPJSON.V)) {
			String statusStr = (String) jo.get(GPJSON.V);
			if (statusStr.equals(GPJSON.OPEN)) {
				status = EGPTrailStatus.GREEN;
			} else if (statusStr.equals(GPJSON.CLOSED)) {
				status = EGPTrailStatus.RED;
			} else {
				status = EGPTrailStatus.YELLOW;
			}
		}
		return status;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public EGPTrailStatus getStatus() {
		return mStatus;
	}

	@Override
	public String getDate() {
		return mGroomed;
	}

	@Override
	public String getDistance() {
		return mDistance;
	}

}
