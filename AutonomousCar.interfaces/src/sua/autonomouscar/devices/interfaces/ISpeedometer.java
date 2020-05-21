package sua.autonomouscar.devices.interfaces;

public interface ISpeedometer {
	
	public int getCurrentSpeed();
	
	public ISpeedometer updateSpeed(int speed); // for simulation purposes only

}
