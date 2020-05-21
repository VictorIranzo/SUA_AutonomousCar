package sua.autonomouscar.distancesensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.devices.DistanceSensor;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected DistanceSensor sensor = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.sensor = new DistanceSensor(bundleContext, "FrontDistanceSensor");
		this.sensor.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.sensor != null )
			this.sensor.unregisterThing();
		this.sensor = null;
		
		Activator.context = null;
	}

}
