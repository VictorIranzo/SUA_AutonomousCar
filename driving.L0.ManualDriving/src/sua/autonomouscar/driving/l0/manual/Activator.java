package sua.autonomouscar.driving.l0.manual;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected L0_ManualDriving drivingFunction = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.drivingFunction = new L0_ManualDriving(bundleContext, "L0_ManualDriving");
		this.drivingFunction.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.drivingFunction != null )
			this.drivingFunction.unregisterThing();
		
		Activator.context = null;
	}

}
