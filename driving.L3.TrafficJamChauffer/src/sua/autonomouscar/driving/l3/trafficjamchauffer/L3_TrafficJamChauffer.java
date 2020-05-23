package sua.autonomouscar.driving.l3.trafficjamchauffer;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ISpeedometer;
import sua.autonomouscar.driving.defaultvalues.*;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.devices.Engine;
import sua.autonomouscar.infrastructure.devices.Steering;
import sua.autonomouscar.infrastructure.driving.L3_DrivingService;
import sua.autonomouscar.interfaces.EFaceStatus;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

public class L3_TrafficJamChauffer extends L3_DrivingService implements IL3_TrafficJamChauffer {
	public L3_TrafficJamChauffer(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL3_TrafficJamChauffer.class.getName());
		this.setReferenceSpeed(L3_TrafficJamChauffer_DefaultValues.DEFAULT_REFERENCE_SPEED);
		this.setLongitudinalSecurityDistance(L3_TrafficJamChauffer_DefaultValues.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);
		this.setLateralSecurityDistance(L3_TrafficJamChauffer_DefaultValues.DEFAULT_LATERAL_SECURITY_DISTANCE);
	}

	
	
	@Override
	public IDrivingService performTheDrivingFunction() {		
		// ADS_L3-1.
		if ( this.getRoadSensor().getRoadType() == ERoadType.OFF_ROAD || this.getRoadSensor().getRoadType() == ERoadType.STD_ROAD) {
			this.debugMessage("Cannot drive in L3 Autonomy level ...");
			this.getNotificationService().notify("Cannot drive in L3 Autonomy level ... Changing to L2 level.");
			
			this.changeToL2Driving();
			
			return this;	
		}
		
		// ADS_L3-4-1.
		if ( this.getRoadSensor().getRoadStatus() == ERoadStatus.FLUID) {
			this.debugMessage("Changing from L3 Traffic Jam Chauffer to Highway chauffer...");
			this.getNotificationService().notify("Changing from L3 Traffic Jam Chauffer to Highway chauffer...");
			
			this.changeToL3HighwayDriving();
			
			return this;	
		}

		//
		// Control de la función primaria: MOVIMIENTO LONGITUDINAL
		//
		
		boolean longitudinal_correction_performed = false;
		ISpeedometer speedometer = OSGiUtils.getService(context, ISpeedometer.class);
		int currentSpeed = speedometer.getCurrentSpeed();
		this.debugMessage(String.format("Current Speed: %d Km/h", currentSpeed));

		// Reducimos la velocidad un poco si detectamos distancia frontal inferior a distancia de seguridad
		if ( this.getLongitudinalSecurityDistance() > this.getFrontDistanceSensor().getDistance() ) {

			this.getEngine().decelerate(Engine.MEDIUM_ACCELERATION_RPM);
			longitudinal_correction_performed = true;
			this.debugMessage("Font Distance Warning: ⊼");
			this.getNotificationService().notify("Font Distance Warning: Braking ...");

		} else {
			

			// Intentamos acercarnos a la velocidad referencia
			int diffSpeed = this.getReferenceSpeed() - currentSpeed;
			
			int rpmCorrection = L3_TrafficJamChauffer_DefaultValues.MY_FINE_ACCELERATION_RPM;
			String rpmAppliedCorrection = "fine";
			if ( Math.abs(diffSpeed) > 30 ) { rpmCorrection = L3_TrafficJamChauffer_DefaultValues.MY_AGGRESSIVE_ACCELERATION_RPM; rpmAppliedCorrection = "aggressive"; }
			else if ( Math.abs(diffSpeed) > 15 ) { rpmCorrection = L3_TrafficJamChauffer_DefaultValues.MY_HIGH_ACCELERATION_RPM; rpmAppliedCorrection = "high"; }
			else if ( Math.abs(diffSpeed) > 5 ) { rpmCorrection = L3_TrafficJamChauffer_DefaultValues.MY_MEDIUM_ACCELERATION_RPM; rpmAppliedCorrection = "medium"; }
			else if ( Math.abs(diffSpeed) > 1 ) { rpmCorrection = L3_TrafficJamChauffer_DefaultValues.MY_SMOOTH_ACCELERATION_RPM; rpmAppliedCorrection = "smooth"; }


			if ( diffSpeed > 0  ) {
				this.getEngine().accelerate(rpmCorrection);
				longitudinal_correction_performed = true;
				this.debugMessage(String.format("Accelerating (%s) to get the reference speeed of %d Km/h", rpmAppliedCorrection, this.getReferenceSpeed()));
			} else if ( diffSpeed < 0 ) {
				this.getEngine().decelerate(rpmCorrection);
				longitudinal_correction_performed = true;
				this.debugMessage(String.format("Decelerating (%s) to get the reference speeed of %d Km/h", rpmAppliedCorrection, this.getReferenceSpeed()));
			}
			
		}

		//
		// Control de la función primaria: MOVIMIENTO LATERAL
		//
		
		boolean lateral_correction_performed = false;
		// Control de las distancias laterales
		if ( this.getRightDistanceSensor().getDistance() < this.getLateralSecurityDistance() ) {
			if ( this.getLeftDistanceSensor().getDistance() > this.getLateralSecurityDistance() ) {
				this.debugMessage("Right Distance Warning: > @ . Turning the Steering to the left ...");
				this.getSteering().rotateLeft(Steering.SEVERE_CORRECTION_ANGLE);
				lateral_correction_performed = true;
			} else {
				this.debugMessage("Right and Left Distance Warning: @ <  > @ . Obstacles too close!!");
				this.getNotificationService().notify("Right and Left Distance Warning: @ <  > @ . Obstacles too close!!");
				lateral_correction_performed = true;
			}
		}

		if ( !lateral_correction_performed && 
			 this.getLeftDistanceSensor().getDistance() < this.getLateralSecurityDistance() ) {
			if ( this.getRightDistanceSensor().getDistance() > this.getLateralSecurityDistance() ) {
				this.debugMessage("Left Distance Warning: @ < . Turning the Steering to the right ...");
				this.getSteering().rotateRight(Steering.SEVERE_CORRECTION_ANGLE);
				lateral_correction_performed = true;
			}
		}

		if ( !lateral_correction_performed ) {
			// Control de la dirección si nos salimos del carril
			if ( this.getLeftLineSensor().isLineDetected() ) {
				this.getSteering().rotateRight(Steering.SMOOTH_CORRECTION_ANGLE);
				lateral_correction_performed = true;
				this.debugMessage("Left Line Sensor Warning: |< . Turning the Steering to the right ...");
				this.getNotificationService().notify("Left Line Sensor Warning: Turning the Steering to the right ...");
			}
			
			if ( this.getRightLineSensor().isLineDetected() ) {
				this.getSteering().rotateLeft(Steering.SMOOTH_CORRECTION_ANGLE);
				lateral_correction_performed = true;
				this.debugMessage("Right Line Sensor Warning: >| . Turning the Steering to the left ...");
				this.getNotificationService().notify("Right Line Sensor Warning: Turning to the left ...");
			}
		}
		
		
			
		// Si todo va bien, indicamos que seguimos como estamos ...
		if ( !longitudinal_correction_performed && !lateral_correction_performed) {
			this.debugMessage("Controlling the driving function. Mantaining the current configuration ...");
		}
		
		
		//
		// Interacción con el conductor
		//

		// Advertimos al humano sí ...

		// ... està distraído o dormido ...
		switch (this.getHumanSensors().getFaceStatus()) {
		case DISTRACTED:
			this.getNotificationService().notify("Please, look forward!");
			break;
		case SLEEPING:
			this.getNotificationService().notify("Please, WAKE UP! ... and look forward!");
			break;
		default:
			break;
		}
		
				
		// ... el conductor no tiene las manos en el volante ...
		if ( !this.getHumanSensors().areTheHandsOnTheWheel() ) {
			this.getNotificationService().notify("Please, put the hands on the wheel!");
		}
		
		// ... el conductor no está en el asiento del conductor ...
		if ( !this.getHumanSensors().isDriverSeatOccupied() ) {
			if ( this.getHumanSensors().isCopilotSeatOccupied() )
				this.getNotificationService().notify("Please, move to the driver seat!");
			else {
				// No se puede conducir en L3 sin conductor. Activamos plan de emergencia
				this.getNotificationService().notify("Cannot drive with a driver! Activating the Fallback Plan ...");
				this.activateTheFallbackPlan();
			}
		}

		
		return this;
	}

	@Override
	public IL3_DrivingService changeToL2Driving() {
		// First, stops driving.
		this.stopDriving();

		// Obtains the registered control and configures it.
		IL2_AdaptiveCruiseControl theL2AdaptiveCruiseControlService = OSGiUtils.getService(context, IL2_AdaptiveCruiseControl.class);
		theL2AdaptiveCruiseControlService.setEngine("Engine");
		theL2AdaptiveCruiseControlService.setFrontDistanceSensor("FrontDistanceSensor");
		
		theL2AdaptiveCruiseControlService.setLongitudinalSecurityDistance(L2_AdaptiveCruiseControl_DefaultValues.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);

		theL2AdaptiveCruiseControlService.setNotificationService("NotificationService");		
		
		// Starts driving with L2 level.
		theL2AdaptiveCruiseControlService.startDriving();
		
		return this;
	}
	
	public IL3_DrivingService changeToL3HighwayDriving() {
		// First, stops driving.
		this.stopDriving();
		
		// Obtains the registered control and configures it.
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		theL3HighwayChaufferService.setHumanSensors("HumanSensors");
		theL3HighwayChaufferService.setRoadSensor("RoadSensor");
		theL3HighwayChaufferService.setEngine("Engine");
		theL3HighwayChaufferService.setSteering("Steering");
		theL3HighwayChaufferService.setFrontDistanceSensor("FrontDistanceSensor");
		theL3HighwayChaufferService.setRearDistanceSensor("RearDistanceSensor");
		theL3HighwayChaufferService.setRightDistanceSensor("RightDistanceSensor");
		theL3HighwayChaufferService.setLeftDistanceSensor("LeftDistanceSensor");
		theL3HighwayChaufferService.setRightLineSensor("RightLineSensor");
		theL3HighwayChaufferService.setLeftLineSensor("LeftLineSensor");
		
		theL3HighwayChaufferService.setReferenceSpeed(L3_HighwayChauffer_DefaultValues.DEFAULT_REFERENCE_SPEED);
		theL3HighwayChaufferService.setLongitudinalSecurityDistance(L3_HighwayChauffer_DefaultValues.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);
		theL3HighwayChaufferService.setLateralSecurityDistance(L3_HighwayChauffer_DefaultValues.DEFAULT_LATERAL_SECURITY_DISTANCE);

		theL3HighwayChaufferService.setNotificationService("NotificationService");		

		theL3HighwayChaufferService.setFallbackPlan("ParkInTheRoadShoulderFallbackPlan");		
		
		// Starts driving with L3 Highway level.
		theL3HighwayChaufferService.startDriving();
		
		return this;
	}
}
