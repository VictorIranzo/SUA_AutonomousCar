package sua.autonomouscar.infrastructure.interaction;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;

public class HapticVibration extends InteractionMechanism {
	
	public HapticVibration(BundleContext context, String device) {
		super(context, String.format("%s_HapticVibration", device));
	}

	@Override
	public IInteractionMechanism performTheInteraction(String message) {

		this.showMessage("〰️");
		return this;
	}





}
