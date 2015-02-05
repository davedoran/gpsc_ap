package dorand.com.gpsc.service.http.impl;

import android.util.Log;

import com.ibm.json.java.JSONObject;

import dorand.com.gpsc.service.http.intf.IGPTrailResponseHandler;
import dorand.com.gpsc.service.impl.GPTrailConditionsResponse;
import dorand.com.gpsc.service.intf.IGPError;
import dorand.com.gpsc.service.util.GPError;
import dorand.com.gpsc.service.util.GPJSONUtils;

public class GPTrailRequest extends GPJSONRequest {

	private static final String NEW_DATE_REGEX = "\\{\"v\"\\s*:\\s*new\\s+Date\\([0-9][0-9][0-9][0-9],[0-9][0-9]?,[0-9][0-9]?\\),";

	private IGPTrailResponseHandler mResponseHandler;

	public GPTrailRequest(String _uri, boolean _useCache, IGPTrailResponseHandler _handler) {
		super(_uri, _useCache);
		mResponseHandler = _handler;
	}

	@Override
	protected void onResponse(JSONObject response) {
		mResponseHandler.onResponse(new GPTrailConditionsResponse(response));
	}
	
	@Override
	protected void onCachedResponse(JSONObject cachedResponse) {
		mResponseHandler.onCachedResponse(new GPTrailConditionsResponse(cachedResponse));
	}	

	@Override
	protected void onError(IGPError err) {
		mResponseHandler.onError(err);
	}

	@Override
	protected JSONObject sanitizeResponse(String responseStr) {
		JSONObject contentJson = null;
		int start = responseStr.indexOf("{");
		int end = responseStr.lastIndexOf("}") + 1;
		String jsonResponse = responseStr.substring(start, end);
		jsonResponse = jsonResponse.replaceAll(NEW_DATE_REGEX, "{");
		jsonResponse = jsonResponse.replaceAll(",,", ",");
		IGPError err = new GPError("GPResponseSanitizer - trail response");
		JSONObject json = GPJSONUtils.parse(jsonResponse.getBytes(), err);
		if (json != null && !err.isError()) {
			contentJson = json;
		} else {
			Log.i("Trail Response Failed: ", jsonResponse);
		}
		return contentJson;
	}

}
