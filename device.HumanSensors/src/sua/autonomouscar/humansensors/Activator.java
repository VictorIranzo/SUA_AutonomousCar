package sua.autonomouscar.humansensors;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.devices.DriverFaceMonitor;
import sua.autonomouscar.infrastructure.devices.HandsOnWheelSensor;
import sua.autonomouscar.infrastructure.devices.HumanSensors;
import sua.autonomouscar.infrastructure.devices.SeatSensor;
import sua.autonomouscar.interfaces.EFaceStatus;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected HumanSensors humanSensors = null;
	protected DriverFaceMonitor driverFaceMonitor = null;
	protected SeatSensor driverSeatSensor = null;
	protected SeatSensor copilotSeatSensor = null;
	protected HandsOnWheelSensor howSensor = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		
		// Driver Face Monitor
		this.driverFaceMonitor = new DriverFaceMonitor(bundleContext, "DriverFaceMonitor");
		this.driverFaceMonitor.setFaceStatus(EFaceStatus.LOOKING_FORWARD);
		this.driverFaceMonitor.registerThing();
		
		
		// Driver Seat Sensor
		this.driverSeatSensor = new SeatSensor(bundleContext, "DriverSeatSensor");
		this.driverSeatSensor.setSeatOccupied(true);
		this.driverSeatSensor.registerThing();
		
		
		// Copilot Seat Sensor
		this.copilotSeatSensor = new SeatSensor(bundleContext, "CopilotSeatSensor");
		this.copilotSeatSensor.registerThing();
		
		// Driver Hands On Wheel Sensor
		this.howSensor = new HandsOnWheelSensor(bundleContext, "HandsOnWheelSensor");
		this.howSensor.registerThing();

		// Human Sensors (aggregate)
		this.humanSensors = new HumanSensors(bundleContext, "HumanSensors");
		this.humanSensors.setFaceMonitor(this.driverFaceMonitor);
		this.humanSensors.setDriverSeatSensor(this.driverSeatSensor);
		this.humanSensors.setCopilotSeatSensor(this.copilotSeatSensor);
		this.humanSensors.setHandsOnWheelSensor(this.howSensor);
		this.humanSensors.registerThing();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.humanSensors != null )
			this.humanSensors.unregisterThing();
		if ( this.driverFaceMonitor != null )
			this.driverFaceMonitor.unregisterThing();
		if ( this.driverSeatSensor != null )
			this.driverSeatSensor.unregisterThing();
		if ( this.copilotSeatSensor != null )
			this.copilotSeatSensor.unregisterThing();
		if ( this.howSensor != null )
			this.howSensor.unregisterThing();
		
		Activator.context = null;
	}

}
