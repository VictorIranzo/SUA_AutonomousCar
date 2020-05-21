package sua.autonomouscar.notificationservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import sua.autonomouscar.infrastructure.interaction.NotificationService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected NotificationService service = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.service = new NotificationService(bundleContext, "NotificationService");
		this.service.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.service != null )
			this.service.unregisterThing();
		
		Activator.context = null;
	}

}
