package sua.autonomouscar.distancesensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.devices.DistanceSensor;


public class Activator implements BundleActivator {

	private static BundleContext context;
	protected DistanceSensor frontDistance_sensor = null;
	protected DistanceSensor rearDistance_sensor = null;
	protected DistanceSensor leftDistance_sensor = null;
	protected DistanceSensor rightDistance_sensor = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		this.frontDistance_sensor = new DistanceSensor(bundleContext, "LIDAR-FrontDistanceSensor");
		this.frontDistance_sensor.registerThing();

		this.rearDistance_sensor = new DistanceSensor(bundleContext, "LIDAR-RearDistanceSensor");
		this.rearDistance_sensor.registerThing();

		this.leftDistance_sensor = new DistanceSensor(bundleContext, "LIDAR-LeftDistanceSensor");
		this.leftDistance_sensor.registerThing();

		this.rightDistance_sensor = new DistanceSensor(bundleContext, "LIDAR-RightDistanceSensor");
		this.rightDistance_sensor.registerThing();
}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.frontDistance_sensor != null )
			this.frontDistance_sensor.unregisterThing();
		if ( this.rearDistance_sensor != null )
			this.rearDistance_sensor.unregisterThing();
		if ( this.leftDistance_sensor != null )
			this.leftDistance_sensor.unregisterThing();
		if ( this.rightDistance_sensor != null )
			this.rightDistance_sensor.unregisterThing();
		
		Activator.context = null;
	}

}
