package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.infrastructure.Thing;

public class LineSensor extends Thing implements ILineSensor {
	
	public static final String DETECTION = "detection";
	
	private boolean isWorking = true;
	
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

	@Override
	public boolean isWorking() {
		return this.isWorking;
	}

	@Override
	public void setIsWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
}
