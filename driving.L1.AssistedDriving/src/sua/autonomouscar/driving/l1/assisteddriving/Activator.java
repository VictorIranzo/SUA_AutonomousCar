
package sua.autonomouscar.driving.l1.assisteddriving;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected L1_AssistedDriving drivingFunction = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.drivingFunction = new L1_AssistedDriving(bundleContext, "L1_AssistedDriving");
		this.drivingFunction.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.drivingFunction != null )
			this.drivingFunction.unregisterThing();
		
		Activator.context = null;
	}

}
