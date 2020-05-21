package sua.autonomouscar.steering;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import sua.autonomouscar.infrastructure.devices.Steering;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected Steering steering = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.steering = new Steering(bundleContext, "Steering");
		this.steering.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.steering != null )
			this.steering.unregisterThing();
		
		Activator.context = null;
	}

}
