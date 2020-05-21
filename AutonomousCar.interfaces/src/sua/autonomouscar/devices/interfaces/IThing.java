package sua.autonomouscar.devices.interfaces;

import sua.autonomouscar.interfaces.IIdentifiable;

public interface IThing extends IIdentifiable {

	public IThing setProperty(String propName, Object value);
	public Object getProperty(String propName);	
	
	public IThing registerThing();
	public IThing unregisterThing();
	public IThing addImplementedInterface(String c);

	
}
