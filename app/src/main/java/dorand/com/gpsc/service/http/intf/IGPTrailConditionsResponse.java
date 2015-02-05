package dorand.com.gpsc.service.http.intf;

import java.util.List;

import dorand.com.gpsc.service.intf.IGPTrailStatus;

public interface IGPTrailConditionsResponse {

	List<IGPTrailStatus> getTrailStatus();

}
