package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.IIdentifiable;

public abstract class L3_DrivingService extends L2_DrivingService implements IL3_DrivingService {

	public final static String REFERENCE_SPEED = "reference-speed";
	
	protected String humanSensors = null;
	protected String roadSensor = null;
	protected String fallbackPlan = null;
	
	protected int lateralSecurityDistance = 1;
	
	public L3_DrivingService(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL3_DrivingService.class.getName());
	}
	
	@Override
	public void setHumanSensors(String humanSensors) {
		this.humanSensors = humanSensors;
		return;
	}

	protected IHumanSensors getHumanSensors() {
		return OSGiUtils.getService(context, IHumanSensors.class, String.format("(%s=%s)", IIdentifiable.ID, this.humanSensors));
	}

	@Override
	public void setRoadSensor(String sensor) {
		this.roadSensor = sensor;
	}
	
	protected IRoadSensor getRoadSensor() {
		return OSGiUtils.getService(context, IRoadSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.roadSensor));
	}
	
	@Override
	public void setFallbackPlan(String plan) {
		this.fallbackPlan = plan;
		return;
	}
	
	protected IFallbackPlan getFallbackPlan() {
		return OSGiUtils.getService(context, IFallbackPlan.class, String.format("(%s=%s)", IIdentifiable.ID, this.fallbackPlan));
	}

	
	@Override
	public void setReferenceSpeed(int speed) {
		this.setProperty(L3_DrivingService.REFERENCE_SPEED, speed);
	}
	
	@Override
	public int getReferenceSpeed() {
		return (int)this.getProperty(L3_DrivingService.REFERENCE_SPEED);
	}

	@Override
	public IL3_DrivingService performTheTakeOver() {
		this.stopDriving();
		this.getNotificationService().notify("Exited Autonomous Mode");
		return this;
	}

	@Override
	public IL3_DrivingService activateTheFallbackPlan() {
		this.stopDriving();
		this.getFallbackPlan().startDriving();

		return this;
	}
}
