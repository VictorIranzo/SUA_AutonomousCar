package sua.autonomouscar.infrastructure.interaction;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;
import sua.autonomouscar.interaction.interfaces.INotificationService;
import sua.autonomouscar.interfaces.IIdentifiable;

public class NotificationService extends Thing implements INotificationService {
	
	protected List<String> mechanisms = null;
		
	public NotificationService(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(INotificationService.class.getName());
		this.mechanisms = new ArrayList<String>();
	}

	@Override
	public INotificationService notify(String message) {
		if ( mechanisms == null || mechanisms.size() == 0 )
			return this;
		
		for(String m : this.mechanisms) {
			IInteractionMechanism mechanism = OSGiUtils.getService(context, IInteractionMechanism.class, String.format("(%s=%s)", IIdentifiable.ID, m));
			mechanism.performTheInteraction(message);
		}
		
		return this;
		
	}

	@Override
	public INotificationService addInteractionMechanism(String m) {
		this.mechanisms.add(m);
		return this;
	}

	@Override
	public INotificationService removeInteractionMechanism(String m) {
		this.mechanisms.remove(m);
		return this;
	}




}
