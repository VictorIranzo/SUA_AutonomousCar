
package sua.autonomouscar.driving.l3.trafficjamchauffer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected L3_TrafficJamChauffer drivingFunction = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.drivingFunction = new L3_TrafficJamChauffer(bundleContext, "L3_TrafficJamChauffer");
		this.drivingFunction.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.drivingFunction != null )
			this.drivingFunction.unregisterThing();
		
		Activator.context = null;
	}

}
