package sua.autonomouscar.driving.l2.lka;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_LaneKeepingAssist;
import sua.autonomouscar.infrastructure.devices.Steering;
import sua.autonomouscar.infrastructure.driving.L2_DrivingService;

public class L2_LaneKeepingAssist extends L2_DrivingService implements IL2_LaneKeepingAssist {
			
	public L2_LaneKeepingAssist(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL2_LaneKeepingAssist.class.getName());
	}


	
	@Override
	public IDrivingService performTheDrivingFunction() {
		
		boolean correction_performed = false;
		
		// Control de la dirección si nos salimos del carril
		if ( this.getLeftLineSensor().isLineDetected() ) {
			this.getSteering().rotateRight(Steering.SMOOTH_CORRECTION_ANGLE);
			correction_performed = true;
			this.debugMessage("Left Line Sensor Warning: |< . Turning the Steering to the right ...");
			this.getNotificationService().notify("Left Line Sensor Warning: Turning the Steering to the right ...");
		}
		
		if ( this.getRightLineSensor().isLineDetected() ) {
			this.getSteering().rotateLeft(Steering.SMOOTH_CORRECTION_ANGLE);
			correction_performed = true;
			this.debugMessage("Right Line Sensor Warning: >| . Turning the Steering to the left ...");
			this.getNotificationService().notify("Right Line Sensor Warning: Turning to the left ...");
		}
		
		// Si todo va bien, indicamos que seguimos como estamos ...
		if ( !correction_performed ) {
			this.debugMessage("Monitoring driving parameters. Nothing to warn about ...");
		}
				
		return this;
	}	
	
	



}
