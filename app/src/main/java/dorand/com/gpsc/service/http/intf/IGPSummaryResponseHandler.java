package dorand.com.gpsc.service.http.intf;

import dorand.com.gpsc.service.intf.IGPError;


public interface IGPSummaryResponseHandler {

	void onResponse(IGPSummaryResponse response);
	void onCachedResponse(IGPSummaryResponse response);
	void onError(IGPError gpError);

}
