package sua.autonomouscar.roadsensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import sua.autonomouscar.infrastructure.devices.RoadSensor;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected RoadSensor roadSensor = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.roadSensor = new RoadSensor(bundleContext, "RoadSensor");
		this.roadSensor.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.roadSensor != null )
			this.roadSensor.unregisterThing();
		
		Activator.context = null;
	}

}
