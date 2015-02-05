package dorand.com.gpsc.service.util;

import android.util.Log;

import dorand.com.gpsc.service.intf.IGPError;

public class GPError implements IGPError {

	private String tag;
	private String error;

	public GPError(String _tag) {
		tag = _tag;
		error = null;
	}
	
	public GPError(String _tag, String _error) {
		tag = _tag;
		error = _error;
		Log.e(tag, error);
	}

	@Override
	public void setError(String _error) {
		Log.e(tag, _error);
		error = _error;
	}

	@Override
	public boolean isError() {
		return error != null;
	}

	@Override
	public String getError() {
		return error;
	}

}
