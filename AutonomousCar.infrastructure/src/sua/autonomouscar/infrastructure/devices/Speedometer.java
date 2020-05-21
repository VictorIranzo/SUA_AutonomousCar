package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ISpeedometer;
import sua.autonomouscar.infrastructure.Thing;

public class Speedometer extends Thing implements ISpeedometer {
	
	public static final String SPEED = "speed";
	public static final int VEHICLE_MAX_SPEED = 240;

	public Speedometer(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ISpeedometer.class.getName());
		this.setCurrentSpeed(0);
	}
	
	
	@Override
	public int getCurrentSpeed() {
		return (int) this.getProperty(Speedometer.SPEED);
	}
	
	protected ISpeedometer setCurrentSpeed(int speed) {
		this.setProperty(Speedometer.SPEED, speed);
		return this;
	}

	
	
	protected IEngine getEngine() {
		ServiceReference<?> engine_ref = this.getBundleContext().getServiceReference(IEngine.class.getName());
		if ( engine_ref == null )
			return null;
		return (IEngine) this.getBundleContext().getService(engine_ref);
	}
	
	
	public int calculateSpeedFromRPM(int rpm) {
		if ( rpm < Engine.MINIMUM_RPM )
			return 0;
		else if ( rpm > Engine.MAXIMUM_RPM )
			return Speedometer.VEHICLE_MAX_SPEED;
		
		return Speedometer.VEHICLE_MAX_SPEED * ( rpm - Engine.MINIMUM_RPM ) / (Engine.MAXIMUM_RPM - Engine.MINIMUM_RPM);
	}
	
	public int calculateRPMFromSpeed(int speed) {
		if ( speed <= 0 )
			return Engine.MINIMUM_RPM;
		else if ( speed > Speedometer.VEHICLE_MAX_SPEED )
			return Engine.MAXIMUM_RPM;
		
		return Engine.MINIMUM_RPM + ( (Engine.MAXIMUM_RPM - Engine.MINIMUM_RPM) * speed / Speedometer.VEHICLE_MAX_SPEED );
	}
	
	@Override
	public ISpeedometer updateSpeed(int rpm) {
		this.setCurrentSpeed(this.calculateSpeedFromRPM(rpm));
		return this;
	}

}
