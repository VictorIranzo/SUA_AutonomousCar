package sua.autonomouscar.interaction.speakers;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.interaction.AuditorySound;
import sua.autonomouscar.infrastructure.interaction.AuditoryBeep;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected AuditorySound im_soundaudio = null;
	protected AuditoryBeep im_soundbeep = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		String deviceId = "Speakers";
		
		this.im_soundaudio = new AuditorySound(bundleContext, deviceId);
		this.im_soundaudio.registerThing();

		this.im_soundbeep = new AuditoryBeep(bundleContext, deviceId);
		this.im_soundbeep.registerThing();

}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.im_soundaudio != null )
			this.im_soundaudio.unregisterThing();

		if ( this.im_soundbeep != null )
			this.im_soundbeep.unregisterThing();

		Activator.context = null;
	}

}
