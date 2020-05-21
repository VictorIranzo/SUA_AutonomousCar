package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ISeatSensor;
import sua.autonomouscar.infrastructure.Thing;

public class SeatSensor extends Thing implements ISeatSensor {
	
	public final static String OCCUPIED = "occupied";

	public SeatSensor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ISeatSensor.class.getName());
		this.setSeatOccupied(false);
	}

	@Override
	public boolean isSeatOccuppied() {
		return (boolean) this.getProperty(SeatSensor.OCCUPIED);
	}
	
	
	@Override
	public ISeatSensor setSeatOccupied(boolean status) {
		this.setProperty(SeatSensor.OCCUPIED, status);
		return this;
	}

}
