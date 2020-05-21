package sua.autonomouscar.interaction.steeringwheel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.interaction.HapticVibration;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected HapticVibration im_hapticvibration = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		String deviceId = "SteeringWheel";
		
		this.im_hapticvibration = new HapticVibration(bundleContext, deviceId);
		this.im_hapticvibration.registerThing();

}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.im_hapticvibration != null )
			this.im_hapticvibration.unregisterThing();

		Activator.context = null;
	}

}
