package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.infrastructure.Thing;

public class Steering extends Thing implements ISteering {
	
	public static final int MAX_ANGLE = 90;
	public static final String ANGLE = "angle";
	public static final int SMOOTH_CORRECTION_ANGLE = 5;
	public static final int MEDIUM_CORRECTION_ANGLE = 15;
	public static final int SEVERE_CORRECTION_ANGLE = 25;
	
	public Steering(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ISteering.class.getName());
		this.center();
	}

	@Override
	public ISteering rotateLeft(int angle) {
		this.setAngle(Math.max(-Steering.MAX_ANGLE, this.getCurrentAngle() - angle));
		return this;
	}

	@Override
	public ISteering rotateRight(int angle) {
		this.setAngle(Math.min(Steering.MAX_ANGLE, this.getCurrentAngle() + angle));
		return this;
	}

	@Override
	public ISteering center() {
		this.setAngle(0);
		return this;
	}

	@Override
	public int getCurrentAngle() {
		return (int) this.getProperty(Steering.ANGLE);
	}
	
	protected ISteering setAngle(int angle) {
		this.setProperty(Steering.ANGLE, angle);
		return this;
	}



}
