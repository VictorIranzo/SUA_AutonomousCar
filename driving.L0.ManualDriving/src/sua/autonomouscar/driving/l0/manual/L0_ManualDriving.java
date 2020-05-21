package sua.autonomouscar.driving.l0.manual;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.infrastructure.driving.DrivingService;
import sua.autonomouscar.simulation.interfaces.ISimulationElement;

public class L0_ManualDriving extends DrivingService implements IL0_ManualDriving, ISimulationElement {
	
	public L0_ManualDriving(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL0_ManualDriving.class.getName());
		this.addImplementedInterface(ISimulationElement.class.getName());
	}

	@Override
	public IDrivingService performTheDrivingFunction() {
		return this;
	}

	@Override
	public IDrivingService stopTheDrivingFunction() {
		return this;
	}

	@Override
	public void onSimulationStep(Integer step, long time_lapse_millis) {
		if ( this.isDriving() )
			this.performTheDrivingFunction();
	}



}
