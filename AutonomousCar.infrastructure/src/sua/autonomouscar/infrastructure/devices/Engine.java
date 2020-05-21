package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.infrastructure.Thing;

public class Engine extends Thing implements IEngine {
	
	public final static String RPM = "rpm";
	public final static int MINIMUM_RPM = 700;
	public final static int MAXIMUM_RPM = 6000;
	
	public final static int LIGHT_ACCELERATION_RPM = 100;
	public final static int MEDIUM_ACCELERATION_RPM = 400;
	public final static int AGGRESSIVE_ACCELERATION_RPM = 1000;
	
	public Engine(BundleContext context, String id) {
		super(context, id);

		this.addImplementedInterface(IEngine.class.getName());
		this.setRPM(Engine.MINIMUM_RPM);

	}
	
	// IEngine
	
	@Override
	public IEngine accelerate(int rpm) {
		if ( rpm < 0 )
			return this.decelerate(Math.abs(rpm));
		
		this.setRPM(Math.min(Engine.MAXIMUM_RPM, this.getCurrentRPM() + rpm));
		return this;
	}

	@Override
	public IEngine decelerate(int rpm) {
		if ( rpm < 0 )
			return this.accelerate(Math.abs(rpm));

		this.setRPM(Math.max(Engine.MINIMUM_RPM, this.getCurrentRPM() - rpm));
		return this;
	}

	@Override
	public IEngine setRPM(int rpm) {
		this.setProperty(Engine.RPM, rpm);
		return this;
	}

	@Override
	public int getCurrentRPM() {
		return (Integer)this.getProperty(Engine.RPM);
	}

}
