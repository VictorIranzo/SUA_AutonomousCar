package sua.autonomouscar.devices.interfaces;

import sua.autonomouscar.interfaces.EFaceStatus;

public interface IHumanSensors {
		
	public boolean isDriverSeatOccupied();
	public boolean isCopilotSeatOccupied();
	public EFaceStatus getFaceStatus();
	public boolean areTheHandsOnTheWheel();
	
	public IHumanSensors setFaceStatus(EFaceStatus status); 		// for simulation purposes only
	public IHumanSensors setDriverSeatOccupancy(boolean value); 	// for simulation purposes only
	public IHumanSensors setCopilotSeatOccupancy(boolean value); 	// for simulation purposes only
	public IHumanSensors setTheHandsOnTheSteeringWheel(boolean value); // for simulation purposes only

	public IHumanSensors setFaceMonitor(IFaceMonitor monitor);
	public IHumanSensors setDriverSeatSensor(ISeatSensor sensor);
	public IHumanSensors setCopilotSeatSensor(ISeatSensor sensor);
	public IHumanSensors setHandsOnWheelSensor(IHandsOnWheelSensor sensor);

}
