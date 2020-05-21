package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.IIdentifiable;

public abstract class L2_DrivingService extends L1_DrivingService implements IL2_DrivingService {

	public final static String LATERAL_SECURITY_DISTANCE = "lateral-security-distance";  // expressed in cms

	protected String engine = null;
	protected String steering = null;
	
	protected String rearDistanceSensor = null;
	protected String rightDistanceSensor = null;
	protected String leftDistanceSensor = null;

	
	public L2_DrivingService(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL2_DrivingService.class.getName());
	}

	@Override
	public void setEngine(String engine) {
		this.engine = engine;
		return;
	}
	
	protected IEngine getEngine() {
		return OSGiUtils.getService(context, IEngine.class, String.format("(%s=%s)", IIdentifiable.ID, this.engine));
	}
	
	@Override
	public void setSteering(String steering) {
		this.steering = steering;
		return;
	}

	protected ISteering getSteering() {
		return OSGiUtils.getService(context, ISteering.class, String.format("(%s=%s)", IIdentifiable.ID, this.steering));
	}
	
	
	@Override
	public void setLateralSecurityDistance(int distance) {
		this.setProperty(L2_DrivingService.LATERAL_SECURITY_DISTANCE, distance);
	}	
	
	@Override
	public int getLateralSecurityDistance() {
		return (int)this.getProperty(L2_DrivingService.LATERAL_SECURITY_DISTANCE);
	}

	
	
	@Override
	public void setRearDistanceSensor(String sensor) {
		this.rearDistanceSensor = sensor;
		return;
	}

	protected IDistanceSensor getRearDistanceSensor() {
		return OSGiUtils.getService(context, IDistanceSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.rearDistanceSensor));
	}

	@Override
	public void setRightDistanceSensor(String sensor) {
		this.rightDistanceSensor = sensor;
		return;
	}

	protected IDistanceSensor getRightDistanceSensor() {
		return OSGiUtils.getService(context, IDistanceSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.rightDistanceSensor));
	}

	@Override
	public void setLeftDistanceSensor(String sensor) {
		this.leftDistanceSensor = sensor;
		return;
	}

	protected IDistanceSensor getLeftDistanceSensor() {
		return OSGiUtils.getService(context, IDistanceSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.leftDistanceSensor));
	}


}
