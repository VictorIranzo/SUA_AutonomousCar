package sua.autonomouscar.driving.interfaces;

public interface IL2_DrivingService extends IL1_DrivingService {
		
	public void setEngine(String engine);
	public void setSteering(String steering);
	
	public void setRearDistanceSensor(String sensor);
	public void setRightDistanceSensor(String sensor);
	public void setLeftDistanceSensor(String sensor);

	public void setLateralSecurityDistance(int distance);
	public int getLateralSecurityDistance();
	
}
