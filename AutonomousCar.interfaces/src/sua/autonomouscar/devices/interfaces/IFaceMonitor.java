package sua.autonomouscar.devices.interfaces;

import sua.autonomouscar.interfaces.EFaceStatus;

public interface IFaceMonitor {

	public EFaceStatus getFaceStatus();
	
	public IFaceMonitor setFaceStatus(EFaceStatus status); // for simulation purposes only
	
}
