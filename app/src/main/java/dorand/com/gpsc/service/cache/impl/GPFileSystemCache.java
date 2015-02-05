package dorand.com.gpsc.service.cache.impl;

import com.ibm.json.java.JSONObject;

import java.io.File;

import dorand.com.gpsc.service.util.GPError;
import dorand.com.gpsc.service.util.GPJSONUtils;
import dorand.com.gpsc.ui.MainActivity;

public class GPFileSystemCache {

	private static final String FILE_FORMAT = "%s/response/%s.json";

	public static void cacheResponse(String key, JSONObject jo) {
		File f = new File(getFilePath(key));
		if (!f.exists() && !f.getParentFile().exists() ) {
			f.mkdirs();
		}
		GPJSONUtils.storeToFile(jo, f, new GPError("GPCache-store"));
	}

	public static JSONObject loadFromCache(String key) {
		JSONObject ret = null;
		File f = new File(getFilePath(key));
		if (f.exists() && !f.isDirectory()) {
			ret = GPJSONUtils.parseFile(f, new GPError("GPCache-load"));
		}
		return ret;
	}
	
	private static String getFilePath(String key) {
		return String.format(FILE_FORMAT, MainActivity.ROOT_DIR, key);
	}

}
