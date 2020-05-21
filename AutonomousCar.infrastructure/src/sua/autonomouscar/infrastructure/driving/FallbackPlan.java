package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interaction.interfaces.INotificationService;
import sua.autonomouscar.interfaces.IIdentifiable;

public abstract class FallbackPlan extends DrivingService implements IFallbackPlan {

	protected String engine = null;
	protected String steering = null;

	protected String notificationService = null;
	
	public FallbackPlan(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IFallbackPlan.class.getName());
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
	public void setNotificationService(String service) {
		this.notificationService = service;
		return;
	}
	
	protected INotificationService getNotificationService() {
		return OSGiUtils.getService(context, INotificationService.class, String.format("(%s=%s)", IIdentifiable.ID, this.notificationService));
	}


	
	

}
