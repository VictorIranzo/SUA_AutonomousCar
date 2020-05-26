package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.infrastructure.Thing;


public class DistanceSensor extends Thing implements IDistanceSensor {
	
	public static final String DISTANCE = "distance";
	public static final int MAX_DISTANCE = 99999;	// expressed in cms
	
	private boolean isWorking = true;
	private String name = "";
	
	public DistanceSensor(BundleContext context, String id) {
		super(context, id);
		this.name = id;
		this.addImplementedInterface(IDistanceSensor.class.getName());
		this.setDistance(DistanceSensor.MAX_DISTANCE);
	}
	
	
	@Override
	public int getDistance() {
		return (int) this.getProperty(DistanceSensor.DISTANCE);
	}

	@Override
	public IDistanceSensor setDistance(int distance) {
		this.setProperty(DistanceSensor.DISTANCE, distance);
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


	@Override
	public String getName() {
		return this.name;
	}
}
