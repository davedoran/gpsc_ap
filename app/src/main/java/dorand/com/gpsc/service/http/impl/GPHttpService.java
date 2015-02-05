package dorand.com.gpsc.service.http.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dorand.com.gpsc.service.http.intf.IGPSummaryResponseHandler;
import dorand.com.gpsc.service.http.intf.IGPTrailResponseHandler;

public class GPHttpService {

	private static final String CONDITIONS_URL = "https://spreadsheets.google.com/tq?key=0AqeNjAYIAcUedGl0SWVZZzNtQ0JNTVFuR1dRQ3psMlE&pub=1&gid=4&tq=SELECT%20B%2CC&tqx=reqId%3A1";
	private static final String CLASSIC_URL = "https://spreadsheets.google.com/tq?key=0AqeNjAYIAcUedGl0SWVZZzNtQ0JNTVFuR1dRQ3psMlE&pub=1&gid=0&tq=SELECT%20B%2CE%2CC%2CF%20%20WHERE%20E%20%3C%3E%20%22%22%20AND%20G%20LIKE%20%22classique%25%22%20ORDER%20BY%20A&tqx=reqId%3A2";
	private static final String SKATE_URL = "https://spreadsheets.google.com/tq?key=0AqeNjAYIAcUedGl0SWVZZzNtQ0JNTVFuR1dRQ3psMlE&pub=1&gid=0&tq=SELECT%20B%2CE%2CC%2CF%20%20WHERE%20E%20%3C%3E%20%22%22%20AND%20G%20LIKE%20%22%25patin%22%20ORDER%20BY%20A&tqx=reqId%3A3";
	private static final String BACKCOUNTRY_URL = "https://spreadsheets.google.com/tq?key=0AqeNjAYIAcUedGl0SWVZZzNtQ0JNTVFuR1dRQ3psMlE&pub=1&gid=0&tq=SELECT%20B%2CE%2CC%2CF%20%20WHERE%20E%20%3C%3E%20%22%22%20AND%20G%20%3D%20%22aucune%20traitement%22%20ORDER%20BY%20A&tqx=reqId%3A4";

	private static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(5);

	public static void getSummaryConditions(final IGPSummaryResponseHandler handler) {
		THREAD_POOL.execute(new GPSummaryRequest(CONDITIONS_URL, true, handler));
	}

	public static void getClassicSkiConditions(final IGPTrailResponseHandler handler) {
		THREAD_POOL.execute(new GPTrailRequest(CLASSIC_URL, true, handler));
	}

	public static void getSkateSkiConditions(final IGPTrailResponseHandler handler) {
		THREAD_POOL.execute(new GPTrailRequest(SKATE_URL, true, handler));
	}

	public static void getBackCountrySkiConditions(final IGPTrailResponseHandler handler) {
		THREAD_POOL.execute(new GPTrailRequest(BACKCOUNTRY_URL, true, handler));
	}

}
