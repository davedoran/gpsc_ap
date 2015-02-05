package dorand.com.gpsc.service.impl;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dorand.com.gpsc.service.http.intf.IGPSummaryResponse;
import dorand.com.gpsc.service.intf.IGPSummaryEntry;
import dorand.com.gpsc.service.util.GPJSON;

public class GPSummaryConditionsResponse implements IGPSummaryResponse {

	private JSONObject mConditions;

	public GPSummaryConditionsResponse(JSONObject jo) {
		mConditions = jo;
	}

	@Override
	public List<IGPSummaryEntry> getSummaryInfo() {
		List<IGPSummaryEntry> ret = new ArrayList<IGPSummaryEntry>();
		if (mConditions != null && mConditions.containsKey(GPJSON.TABLE)) {
			JSONObject table = (JSONObject) mConditions.get(GPJSON.TABLE);
			if (table.containsKey(GPJSON.COLS)) {
				parseCols((JSONArray) table.get(GPJSON.COLS), ret);
			}

			if (table.containsKey(GPJSON.ROWS)) {
				parseRows((JSONArray) table.get(GPJSON.ROWS), ret);
			}
		}
		return ret;
	}

	private void parseCols(JSONArray jsonArray, List<IGPSummaryEntry> ret) {
		String name = null;
		String value = null;
		if (jsonArray != null && jsonArray.size() == 2) {
			JSONObject key = (JSONObject) jsonArray.get(0);
			name = (String) key.get(GPJSON.LABEL);

			JSONObject val = (JSONObject) jsonArray.get(1);
			value = (String) val.get(GPJSON.LABEL);
		}

		if (name != null && value != null) {
			ret.add(new GPSummaryEntry(name.trim(), value.trim()));
		}
	}

	private void parseRows(JSONArray jsonArray, List<IGPSummaryEntry> ret) {
		for (Object o : jsonArray) {
			if (o instanceof JSONObject) {
				JSONObject jo = (JSONObject) o;
				if (jo.containsKey(GPJSON.C)) {
					JSONArray ja = (JSONArray) jo.get(GPJSON.C);
					if (ja != null && ja.size() == 2) {
						JSONObject key = (JSONObject) ja.get(0);
						String name = (String) key.get(GPJSON.V);

						JSONObject val = (JSONObject) ja.get(1);
						String value = (String) val.get(GPJSON.V);

						if (name != null && value != null) {
							ret.add(new GPSummaryEntry(name.trim(), value.trim()));
						}
					}
				}
			}
		}
	}

}
