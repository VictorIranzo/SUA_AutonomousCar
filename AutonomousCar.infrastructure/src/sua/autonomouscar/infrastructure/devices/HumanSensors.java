package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IFaceMonitor;
import sua.autonomouscar.devices.interfaces.IHandsOnWheelSensor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ISeatSensor;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.EFaceStatus;

public class HumanSensors extends Thing implements IHumanSensors {
	
	protected IFaceMonitor faceMonitor = null;
	protected ISeatSensor driverSeatSensor = null;
	protected ISeatSensor copilotSeatSensor = null;
	protected IHandsOnWheelSensor howSensor = null;
	
	public HumanSensors(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IHumanSensors.class.getName());
	}
	
	@Override
	public IHumanSensors setFaceMonitor(IFaceMonitor monitor) {
		this.faceMonitor = monitor;
		return this;
	}
	
	
	@Override
	public IHumanSensors setCopilotSeatSensor(ISeatSensor sensor) {
		this.copilotSeatSensor = sensor;
		return this;
	}
	
	@Override
	public IHumanSensors setDriverSeatSensor(ISeatSensor sensor) {
		this.driverSeatSensor = sensor;
		return this;
	}
	
	@Override
	public IHumanSensors setHandsOnWheelSensor(IHandsOnWheelSensor sensor) {
		this.howSensor = sensor;
		return this;
	}
	

	@Override
	public boolean isDriverSeatOccupied() {
		if ( this.driverSeatSensor != null )
			return this.driverSeatSensor.isSeatOccuppied();
		return false;
	}

	@Override
	public boolean isCopilotSeatOccupied() {
		if ( this.copilotSeatSensor != null )
			return this.copilotSeatSensor.isSeatOccuppied();
		return false;
	}

	@Override
	public EFaceStatus getFaceStatus() {
		if ( this.faceMonitor != null )
			return this.faceMonitor.getFaceStatus();
		
		return EFaceStatus.UNKNOWN;
	}
	
	@Override
	public boolean areTheHandsOnTheWheel() {
		if ( this.howSensor != null )
			return this.howSensor.areTheHandsOnTheSteeringWheel();
		return false;
	}

	
	@Override
	public IHumanSensors setFaceStatus(EFaceStatus status) {
		if ( this.faceMonitor != null )
			this.faceMonitor.setFaceStatus(status);
		return this;
	}

	@Override
	public IHumanSensors setDriverSeatOccupancy(boolean value) {
		if ( this.driverSeatSensor != null )
			this.driverSeatSensor.setSeatOccupied(value);
		return this;
	}

	@Override
	public IHumanSensors setCopilotSeatOccupancy(boolean value) {
		if ( this.copilotSeatSensor != null )
			this.copilotSeatSensor.setSeatOccupied(value);
		return this;
	}
	
	@Override
	public IHumanSensors setTheHandsOnTheSteeringWheel(boolean value) {
		if ( this.howSensor != null )
			this.howSensor.setTheHandsOnTheSteeringWheel(value);
		return this;
	}


}
