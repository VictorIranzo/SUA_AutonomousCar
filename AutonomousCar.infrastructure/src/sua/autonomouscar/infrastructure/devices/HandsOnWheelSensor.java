package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IHandsOnWheelSensor;
import sua.autonomouscar.infrastructure.Thing;

public class HandsOnWheelSensor extends Thing implements IHandsOnWheelSensor {
	
	public final static String HANDS_ON_WHEEL = "hands-on-wheel";

	public HandsOnWheelSensor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IHandsOnWheelSensor.class.getName());
		this.setTheHandsOnTheSteeringWheel(true);
	}

	@Override
	public boolean areTheHandsOnTheSteeringWheel() {
		return (boolean) this.getProperty(HandsOnWheelSensor.HANDS_ON_WHEEL);
	}
	
	
	@Override
	public IHandsOnWheelSensor setTheHandsOnTheSteeringWheel(boolean status) {
		this.setProperty(HandsOnWheelSensor.HANDS_ON_WHEEL, status);
		return this;
	}

}
