package sua.autonomouscar.infrastructure.interaction;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;

public abstract class InteractionMechanism extends Thing implements IInteractionMechanism {
	
	public InteractionMechanism(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IInteractionMechanism.class.getName());
	}
	
	protected void showMessage(String message) {
		System.out.println(String.format("## %35s ## %s", this.getId(), message));
	}

}
