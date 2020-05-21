package sua.autonomouscar.infrastructure.interaction;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;

public class AuditoryBeep extends InteractionMechanism {
	
	public AuditoryBeep(BundleContext context, String device) {
		super(context, String.format("%s_AuditoryBeep", device));
	}

	@Override
	public IInteractionMechanism performTheInteraction(String message) {
		this.showMessage("🔔");
		return this;
	}





}
