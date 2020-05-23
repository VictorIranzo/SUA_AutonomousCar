package sua.autonomouscar.devices.interfaces;

public interface IDistanceSensor {
	
	public int getDistance();

	public boolean isWorking();
	public void setIsWorking(boolean isWorking);
	
	public IDistanceSensor setDistance(int distance); // for simulation purposes only
}
