package dorand.com.gpsc.service.http.intf;

import java.util.List;

import dorand.com.gpsc.service.intf.IGPSummaryEntry;

public interface IGPSummaryResponse {

	List<IGPSummaryEntry> getSummaryInfo();

}
