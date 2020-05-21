package sua.autonomouscar.infrastructure.interaction;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;

public class VisualText extends InteractionMechanism {
	
	public VisualText(BundleContext context, String device) {
		super(context, String.format("%s_VisualText", device));
	}

	@Override
	public IInteractionMechanism performTheInteraction(String message) {

		this.showMessage(message);
		return this;
	}





}
