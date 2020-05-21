package sua.autonomouscar.speedometer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.infrastructure.devices.Speedometer;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected Speedometer odometer = null;
	protected SpeedChangeListener listener = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.odometer = new Speedometer(bundleContext, "Odometer");
		this.odometer.registerThing();
		
		this.listener = new SpeedChangeListener(bundleContext, this.odometer);
		String filter = "(" + Constants.OBJECTCLASS + "=" + IEngine.class.getName() + ")";
		bundleContext.addServiceListener(this.listener, filter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.odometer != null )
			this.odometer.unregisterThing();
		if ( this.listener != null )
			bundleContext.removeServiceListener(this.listener);
		
		Activator.context = null;
	}

}
