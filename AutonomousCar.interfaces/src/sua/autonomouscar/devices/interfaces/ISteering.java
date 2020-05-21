package sua.autonomouscar.devices.interfaces;

public interface ISteering {

	public ISteering rotateLeft(int angle);
	public ISteering rotateRight(int angle);
	public ISteering center();
	public int getCurrentAngle();
}
