package sua.autonomouscar.driving.l2.acc;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected L2_AdaptiveCruiseControl drivingFunction = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.drivingFunction = new L2_AdaptiveCruiseControl(bundleContext, "L2_AdaptiveCruiseControl");
		this.drivingFunction.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.drivingFunction != null )
			this.drivingFunction.unregisterThing();
		
		Activator.context = null;
	}

}
