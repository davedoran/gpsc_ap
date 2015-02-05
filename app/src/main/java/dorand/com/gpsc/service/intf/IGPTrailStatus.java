package dorand.com.gpsc.service.intf;

public interface IGPTrailStatus {

	public enum EGPTrailStatus {
		GREEN, YELLOW, RED, CLEAR
	}

	String getName();

	EGPTrailStatus getStatus();

	String getDate();

	String getDistance();

}
