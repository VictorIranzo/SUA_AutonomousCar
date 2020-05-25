package sua.autonomouscar.driving.interfaces;

import sua.autonomouscar.interaction.interfaces.INotificationService;

public interface IL1_DrivingService extends IL0_DrivingService {
	
	public void setFrontDistanceSensor(String sensor);
	public void setRightLineSensor(String sensor);
	public void setLeftLineSensor(String sensor);
	
	public void setLongitudinalSecurityDistance(int distance);
	public int getLongitudinalSecurityDistance();
	
	public void setNotificationService(String service);
	public INotificationService getNotificationService();
}
