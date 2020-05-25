package sua.autonomouscar.driving.interfaces;

import sua.autonomouscar.devices.interfaces.IHumanSensors;

public interface IL3_DrivingService extends IL2_DrivingService {
	
	public void setHumanSensors(String humanSensors);
	public IHumanSensors getHumanSensors();
	
	public void setRoadSensor(String roadSensors);
	
	public void setFallbackPlan(String plan);
	public IFallbackPlan getFallbackPlan();
	
	public void setReferenceSpeed(int speed);
	public int getReferenceSpeed();
	
	public IL3_DrivingService performTheTakeOver();
	public IL3_DrivingService activateTheFallbackPlan();
	
}
