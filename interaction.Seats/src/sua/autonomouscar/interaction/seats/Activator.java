package sua.autonomouscar.interaction.seats;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.interaction.HapticVibration;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected HapticVibration im_hapticvibration_driverSeat = null;
	protected HapticVibration im_hapticvibration_copilotSeat = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
				
		this.im_hapticvibration_driverSeat = new HapticVibration(bundleContext, "DriverSeat");
		this.im_hapticvibration_driverSeat.registerThing();

		this.im_hapticvibration_copilotSeat = new HapticVibration(bundleContext, "CopilotSeat");
		this.im_hapticvibration_copilotSeat.registerThing();

}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.im_hapticvibration_driverSeat != null )
			this.im_hapticvibration_driverSeat.unregisterThing();

		if ( this.im_hapticvibration_copilotSeat != null )
			this.im_hapticvibration_copilotSeat.unregisterThing();

		Activator.context = null;
	}

}
