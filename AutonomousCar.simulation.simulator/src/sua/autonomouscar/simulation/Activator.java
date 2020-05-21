package sua.autonomouscar.simulation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	protected ISimulator simulator = null;
	protected ISimulatorStepsManager stepsManager = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		this.simulator = new Simulator(bundleContext, "AutonomousCar.Simulator");
		String simulatorType = System.getProperty("simulator.type");
		if ( simulatorType == null || !simulatorType.startsWith("time") ) {
			this.stepsManager = new ManualSimulatorStepsManager(context, this.simulator);
		} else {
			this.stepsManager = new TimedSimulatorStepsManager(context, this.simulator);
			String simulationStepTimeInMillis = System.getProperty("simulator.period");
			int period = 3000;
			try {
				period = Integer.valueOf(simulationStepTimeInMillis);
			} catch (Exception e) {
			}
			
			((TimedSimulatorStepsManager)this.stepsManager).setSimulationTimeSteps(period);
		}
		
		this.simulator.setStepsManager(this.stepsManager);
		boolean verboseMode = false;
		try {
			verboseMode = Boolean.valueOf(System.getProperty("simulator.verbose"));
		} catch (Exception e) {
		}
		this.simulator.setVerboseMode(verboseMode);
		this.simulator.start();
		
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		
		if ( this.simulator != null )
			this.simulator.stop();
		this.simulator = null;
		this.stepsManager = null;
		
		Activator.context = null;
	}


}
