package sua.autonomouscar.linesensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.devices.LineSensor;


public class Activator implements BundleActivator {

	private static BundleContext context;
	protected LineSensor sensor = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.sensor = new LineSensor(bundleContext, "RightLineSensor");
		this.sensor.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.sensor != null )
			this.sensor.unregisterThing();
		
		Activator.context = null;
	}

}
