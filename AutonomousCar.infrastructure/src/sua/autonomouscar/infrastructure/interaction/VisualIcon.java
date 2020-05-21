package sua.autonomouscar.infrastructure.interaction;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;

public class VisualIcon extends InteractionMechanism {
	
	
	public VisualIcon(BundleContext context, String device) {
		super(context, String.format("%s_VisualIcon", device));
	}

	@Override
	public IInteractionMechanism performTheInteraction(String message) {
		// 🗣
		this.showMessage("⚠️");
		return this;
	}





}
