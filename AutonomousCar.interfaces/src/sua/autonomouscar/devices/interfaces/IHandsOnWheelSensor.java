package sua.autonomouscar.devices.interfaces;

public interface IHandsOnWheelSensor {

	public boolean areTheHandsOnTheSteeringWheel();
	
	public IHandsOnWheelSensor setTheHandsOnTheSteeringWheel(boolean value); // for simulation purposes only
}
