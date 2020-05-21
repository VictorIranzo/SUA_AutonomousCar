package sua.autonomouscar.driving.interfaces;


public interface IFallbackPlan extends IDrivingService {
	
	public void setEngine(String engine);
	public void setSteering(String steering);
	
	public void setNotificationService(String service);


}
