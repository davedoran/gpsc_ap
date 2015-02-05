package dorand.com.gpsc.service.http.impl;

import com.ibm.json.java.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import dorand.com.gpsc.service.cache.impl.GPFileSystemCache;
import dorand.com.gpsc.service.http.intf.IGPCachedResponse;
import dorand.com.gpsc.service.intf.IGPError;
import dorand.com.gpsc.service.util.GPError;

/**
 * Issues an HTTP Request that expects a response that can be parsed into a
 * JSONObject. Briefly caches responses...
 */
public abstract class GPJSONRequest implements Runnable {

	private String mUrl;
	private boolean mUseCache;

	private static final Map<String, IGPCachedResponse> CACHE = new HashMap<String, IGPCachedResponse>();

	protected GPJSONRequest(String _url, boolean _useCache) {
		mUrl = _url;
		mUseCache = _useCache;
	}

	@Override
	public void run() {
		if (mUseCache) {
			IGPCachedResponse fromCache = null;
			synchronized (CACHE) {
				fromCache = CACHE.get(mUrl);
			}
			if (fromCache != null && !fromCache.isExpired()) {
				onResponse(fromCache.getResponse());
			} else {
				runRequest();
			}
		} else {
			runRequest();
		}

	}

	private void runRequest() {
		URI uri = null;
		try {
			uri = new URI(mUrl);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(uri);
		HttpResponse response = null;
		try {
			response = client.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					String result = convertStreamToString(instream);
					instream.close();
					JSONObject resultJSON = sanitizeResponse(result);
					if (resultJSON != null) {
						cacheToFileSystem(resultJSON);
						onResponse(resultJSON);
						synchronized (CACHE) {
							CACHE.put(mUrl, new GPCachedResponse(resultJSON));
						}
					} else {
						onError(new GPError(getClass().getName(), "Failed to parse JSON response..."));
					}
				} else {
					serveCachedResponseIfAvailable(new GPError(getClass().getName(), "Entity not found..."));
				}
			} else {
				serveCachedResponseIfAvailable(new GPError(getClass().getName(), "Response code: " + response.getStatusLine().getStatusCode()));
			}
		} catch (IOException e) {
			serveCachedResponseIfAvailable(new GPError(getClass().getName(), e.getMessage()));
		}
	}

	private void serveCachedResponseIfAvailable(GPError err) {
		JSONObject fromCache = loadFromFileSystem();
		if (fromCache != null) {
			onCachedResponse(fromCache);
		} else {
			onError(err);
		}
	}

	protected abstract JSONObject sanitizeResponse(String result);

	protected abstract void onResponse(JSONObject response);

	protected abstract void onCachedResponse(JSONObject cachedResponse);

	protected abstract void onError(IGPError err);

	protected void cacheToFileSystem(JSONObject response) {
		GPFileSystemCache.cacheResponse(genCacheKey(), response);
	}

	protected JSONObject loadFromFileSystem() {
		return GPFileSystemCache.loadFromCache(genCacheKey());
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private String genCacheKey() {
		String ret = "";
		final String MD5 = "MD5";
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance(MD5);

			digest.update(mUrl.toString().getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			ret = hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
