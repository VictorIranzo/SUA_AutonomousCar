package sua.autonomouscar.interaction.interfaces;

import sua.autonomouscar.interfaces.IIdentifiable;

public interface INotificationService extends IIdentifiable {
	
	public INotificationService notify(String message);
	
	public INotificationService addInteractionMechanism(String mechanism);
	public INotificationService removeInteractionMechanism(String mechanism);

}
