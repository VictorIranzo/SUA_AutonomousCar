package sua.autonomouscar.engine;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import sua.autonomouscar.infrastructure.devices.Engine;


public class Activator implements BundleActivator {

	private static BundleContext context;
	private Engine engine = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		this.engine = new Engine(bundleContext, "Engine");
		this.engine.registerThing();
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.engine != null )
			this.engine.unregisterThing();

		Activator.context = null;
	}

}
