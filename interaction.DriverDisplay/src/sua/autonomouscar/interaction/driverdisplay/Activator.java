package sua.autonomouscar.interaction.driverdisplay;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.interaction.VisualIcon;
import sua.autonomouscar.infrastructure.interaction.VisualText;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected VisualText im_visualtext = null;
	protected VisualIcon im_visualicon = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		String deviceId = "DriverDisplay";
		
		this.im_visualtext = new VisualText(bundleContext, deviceId);
		this.im_visualtext.registerThing();

		this.im_visualicon = new VisualIcon(bundleContext, deviceId);
		this.im_visualicon.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.im_visualtext != null )
			this.im_visualtext.unregisterThing();

		if ( this.im_visualicon != null )
			this.im_visualicon.unregisterThing();

		Activator.context = null;
	}

}
