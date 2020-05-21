package sua.autonomouscar.interaction.interfaces;

import sua.autonomouscar.devices.interfaces.IThing;

public interface IInteractionMechanism extends IThing {

	public IInteractionMechanism performTheInteraction(String message);
	
}
