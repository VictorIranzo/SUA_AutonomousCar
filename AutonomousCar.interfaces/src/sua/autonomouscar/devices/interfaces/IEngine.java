package sua.autonomouscar.devices.interfaces;

public interface IEngine {
		
	public IEngine accelerate(int rpm);
	public IEngine decelerate(int rpm);
	public IEngine setRPM(int rpm);
	
	public int getCurrentRPM();

}
