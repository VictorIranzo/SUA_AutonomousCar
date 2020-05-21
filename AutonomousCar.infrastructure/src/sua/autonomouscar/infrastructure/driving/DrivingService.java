package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.interfaces.IDrivingService;
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

	

	
	
	// ISimulationElement
	@Override
	public void onSimulationStep(Integer step, long time_lapse_millis) {
		if ( this.isDriving() )
			this.performTheDrivingFunction();
	}

	
}
