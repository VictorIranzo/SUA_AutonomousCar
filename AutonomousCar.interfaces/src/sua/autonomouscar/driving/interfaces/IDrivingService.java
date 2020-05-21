package sua.autonomouscar.driving.interfaces;

import sua.autonomouscar.interfaces.IIdentifiable;

public interface IDrivingService extends IIdentifiable{
	
	public IDrivingService startDriving();
	public IDrivingService stopDriving();
	public boolean isDriving();

}
