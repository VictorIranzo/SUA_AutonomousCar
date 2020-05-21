package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.infrastructure.Thing;

public class LineSensor extends Thing implements ILineSensor {
	
	public static final String DETECTION = "detection";
	
	public LineSensor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ILineSensor.class.getName());
		this.setLineDetected(false);
	}

	@Override
	public boolean isLineDetected() {
		return (boolean) this.getProperty(LineSensor.DETECTION);
	}
	
	@Override
	public ILineSensor setLineDetected(boolean value) {
		this.setProperty(LineSensor.DETECTION, value);
		return this;
	}



}
