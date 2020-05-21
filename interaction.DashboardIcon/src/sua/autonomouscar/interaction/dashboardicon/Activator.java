package sua.autonomouscar.interaction.dashboardicon;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.interaction.VisualIcon;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected VisualIcon im_visualicon = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		String deviceId = "DashboardIcon";
		
		this.im_visualicon = new VisualIcon(bundleContext, deviceId);
		this.im_visualicon.registerThing();

	}

	public void stop(BundleContext bundleContext) throws Exception {

		if ( this.im_visualicon != null )
			this.im_visualicon.unregisterThing();

		Activator.context = null;
	}

}
