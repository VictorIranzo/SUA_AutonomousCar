package sua.autonomouscar.devices.interfaces;

public interface ILineSensor {
	
	public boolean isLineDetected();
	
	public ILineSensor setLineDetected(boolean value); // for simulation purposes only

	public boolean isWorking();
	public void setIsWorking(boolean isWorking);
}
