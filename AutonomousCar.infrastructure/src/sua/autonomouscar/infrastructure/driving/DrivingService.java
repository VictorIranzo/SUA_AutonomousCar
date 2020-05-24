package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.defaultvalues.L2_AdaptiveCruiseControl_DefaultValues;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.simulation.interfaces.ISimulationElement;


public abstract class DrivingService extends Thing implements IDrivingService, ISimulationElement {
	
	public static final String ACTIVE = "active";

	public DrivingService(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IDrivingService.class.getName());
		this.addImplementedInterface(ISimulationElement.class.getName());
		this.setProperty(DrivingService.ACTIVE, false);
	}

	@Override
	public IDrivingService startDriving() {
		if ( this.isDriving() )
			return this;
		
		this.debugMessage("Starting the driving function ...");
		this.setProperty(DrivingService.ACTIVE, true);
		this.performTheDrivingFunction();
		return this;
	}

	@Override
	public IDrivingService stopDriving() {
		if ( !this.isDriving() )
			return this;
		
		this.debugMessage("Ending the driving function ...");
		this.setProperty(DrivingService.ACTIVE, false);
		this.stopTheDrivingFunction();
		return this;
	}

	@Override
	public boolean isDriving() {
		return (boolean) this.getProperty(DrivingService.ACTIVE);
	}

	public abstract IDrivingService performTheDrivingFunction();
	public abstract IDrivingService stopTheDrivingFunction();
	
	protected void debugMessage(String msg) {
		System.out.println("[ " + this.getId() + " ] " + msg);
	}

	private boolean isWorking = true;

	@Override
	public boolean isWorking() {
		return this.isWorking;
	}

	@Override
	public void setIsWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
	
	// ISimulationElement
	@Override
	public void onSimulationStep(Integer step, long time_lapse_millis) {
		if ( this.isDriving() )
			this.performTheDrivingFunction();
	}

	public DrivingService changeToL0Driving() {
		// First, stops driving.
		this.stopDriving();

		// Obtains the registered control and configures it.
		IL0_ManualDriving manualDriving = OSGiUtils.getService(context, IL0_ManualDriving.class);	
		
		// Starts driving with L2 level.
		manualDriving.startDriving();
		
		return this;
	}
}
