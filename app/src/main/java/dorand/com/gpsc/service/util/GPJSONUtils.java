package dorand.com.gpsc.service.util;

import android.util.Log;

import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;

import dorand.com.gpsc.service.intf.IGPError;

public class GPJSONUtils {

	public static JSONArray parseJSONArray(byte[] data, IGPError error) {
		try {
			return (JSONArray) JSON.parse(new ByteArrayInputStream(data));
		} catch (Exception e) {
			if (error != null) {
				error.setError(e.getMessage());
			}
		}
		return null;
	}

	public static JSONObject parseString(String jsonString, IGPError error) {
		try {
			return (JSONObject) JSON.parse(jsonString);
		} catch (Exception e) {
			if (error != null) {
				error.setError(e.getMessage());
			}
		}
		return null;
	}

	public static JSONObject parse(byte[] data, IGPError error) {
		try {
			return (JSONObject) JSON.parse(new ByteArrayInputStream(data));
		} catch (Exception e) {
			if (error != null) {
				error.setError(e.getMessage());
			}
		}
		return null;
	}

	// parse from file, intended for testing.
	public static JSONObject parseFile(File file, IGPError error) {
		JSONObject ret = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			ret = (JSONObject) JSON.parse(is);
		} catch (IOException x) {
			error.setError(x.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
					// n/m
				}
			}
		}
		return ret;
	}

    public static JSONObject parseStream(InputStream is, IGPError error) {
        JSONObject ret = null;
        try {
            ret = (JSONObject)JSON.parse(is);
        } catch (IOException x) {
            error.setError(x.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    // n/m
                }
            }
        }
        return ret;
    }

	public static byte[] serialize(JSONObject jo) {
		if (jo != null) {
			try {
				return jo.serialize().getBytes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void storeToFile(JSONObject jo, File file, IGPError error) {
		if (jo != null) {
			byte[] data = GPJSONUtils.serialize(jo);
			if (data != null) {
				if (file.exists()) {
					file.delete();
				}
				String filePath = file.getAbsolutePath();
				Log.i("GPJSONUtils", filePath);
				FileWriter fw = null;
				try {
					file.createNewFile();					
					fw = new FileWriter(file);
					fw.write(new String(data));
				} catch ( IOException ex ) {
					error.setError(ex.getMessage());
				} finally {
					if ( fw != null ) {
						try {
							fw.close();
						} catch ( IOException ex ) {
							// n/m
						}
					}
				}
			} else {
				error.setError("Unable to serialize JSONObject");
			}
		}

	}
}
