package sua.autonomouscar.simulation.console.commands;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class NotificationServiceConfigurator {
	
	public static void configure(BundleContext context) {
		
		INotificationService service = OSGiUtils.getService(context, INotificationService.class);
		
		service.addInteractionMechanism("DriverDisplay_VisualText");
		service.addInteractionMechanism("Speakers_AuditoryBeep");
		service.addInteractionMechanism("SteeringWheel_HapticVibration");

		// Configure with all available mechanisms ...
//		List<IInteractionMechanism> mechanisms = OSGiUtils.getServices(context, IInteractionMechanism.class);
//		if ( mechanisms != null && mechanisms.size() > 0 )
//			for(IInteractionMechanism m : mechanisms)
//				service.addInteractionMechanism(m.getId());

		
	}

}
