package sua.autonomouscar.devices.interfaces;

public interface ISeatSensor {
	
	public boolean isSeatOccuppied();
	
	public ISeatSensor setSeatOccupied(boolean value); // for simulation purposes only

}
