package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;

public abstract class L0_DrivingService extends DrivingService implements IL0_DrivingService {

	
	public L0_DrivingService(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL0_DrivingService.class.getName());
	}



	@Override
	public IDrivingService stopTheDrivingFunction() {
		return this;
	}



}
