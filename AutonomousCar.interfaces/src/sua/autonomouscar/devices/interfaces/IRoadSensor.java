package sua.autonomouscar.devices.interfaces;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

public interface IRoadSensor {

	public ERoadType getRoadType();
	public ERoadStatus getRoadStatus();
	
	public IRoadSensor setRoadType(ERoadType type);  // for simulation purposes only
	public IRoadSensor setRoadStatus(ERoadStatus s); // for simulation purposes only
	
}
