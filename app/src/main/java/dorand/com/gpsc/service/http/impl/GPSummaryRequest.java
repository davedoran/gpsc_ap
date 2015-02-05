package dorand.com.gpsc.service.http.impl;

import android.util.Log;

import com.ibm.json.java.JSONObject;

import dorand.com.gpsc.service.http.intf.IGPSummaryResponseHandler;
import dorand.com.gpsc.service.impl.GPSummaryConditionsResponse;
import dorand.com.gpsc.service.intf.IGPError;
import dorand.com.gpsc.service.util.GPError;
import dorand.com.gpsc.service.util.GPJSONUtils;

public class GPSummaryRequest extends GPJSONRequest {

	private IGPSummaryResponseHandler mResponseHandler;

	public GPSummaryRequest(String _url, boolean _useCache, IGPSummaryResponseHandler _handler) {
		super(_url, _useCache);
		mResponseHandler = _handler;
	}

	@Override
	protected void onResponse(JSONObject response) {
		mResponseHandler.onResponse(new GPSummaryConditionsResponse(response));
	}

	@Override
	protected void onCachedResponse(JSONObject cachedResponse) {
		mResponseHandler.onCachedResponse(new GPSummaryConditionsResponse(cachedResponse));
	}

	@Override
	protected void onError(IGPError err) {
		mResponseHandler.onError(err);
	}

	@Override
	protected JSONObject sanitizeResponse(String str) {
		JSONObject contentJson = null;
		int start = str.indexOf("{");
		int end = str.lastIndexOf("}") + 1;
		String jsonResponse = str.substring(start, end);
		IGPError err = new GPError(getClass().getName());
		JSONObject json = GPJSONUtils.parse(jsonResponse.getBytes(), err);
		if (json != null && !err.isError()) {
			contentJson = json;
		} else {
			Log.i("Summary Response Failed: ", jsonResponse);
		}
		return contentJson;
	}

}
