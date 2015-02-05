package dorand.com.gpsc.service.http.intf;

import dorand.com.gpsc.service.impl.GPTrailConditionsResponse;
import dorand.com.gpsc.service.intf.IGPError;

public interface IGPTrailResponseHandler {

	void onResponse(IGPTrailConditionsResponse response);

	void onError(IGPError error);

	void onCachedResponse(GPTrailConditionsResponse gpTrailConditionsResponse);

}
