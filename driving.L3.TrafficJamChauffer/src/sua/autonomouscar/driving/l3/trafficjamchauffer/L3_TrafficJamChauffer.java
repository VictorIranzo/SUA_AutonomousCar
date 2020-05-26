package sua.autonomouscar.driving.l3.trafficjamchauffer;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.ISpeedometer;
import sua.autonomouscar.driving.defaultvalues.*;
import sua.autonomouscar.driving.emergencyfallbackplan.EmergencyFallbackPlan;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
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
import sua.autonomouscar.interfaces.IIdentifiable;

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
		// ADS-1
		if (this.getFrontDistanceSensor().getName().contains("LIDAR"))
		{
			// Comprobamos si los sensores de distancia dedicados est�n disponibles, para emplearlos.
			IDistanceSensor FrontDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=FrontDistanceSensor)");
			IDistanceSensor RearDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=RearDistanceSensor)");
			IDistanceSensor RightDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=RightDistanceSensor)");
			IDistanceSensor LeftDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LeftDistanceSensor)");
	
		    boolean isWorkingDistanceSensor = FrontDistanceSensor.isWorking() && RearDistanceSensor.isWorking() 
		    		&& RightDistanceSensor.isWorking() && LeftDistanceSensor.isWorking();
		    
		    if (isWorkingDistanceSensor)
		    {
				this.debugMessage("Replacing LIDAR by Distance sensor.");

		    	this.setFrontDistanceSensor("FrontDistanceSensor");
		    	this.setRearDistanceSensor("RearDistanceSensor");
		    	this.setRightDistanceSensor("RightDistanceSensor");
		    	this.setLeftDistanceSensor("LeftDistanceSensor");
		    }
		}
		
		// INTERACT-1
		if (this.getHumanSensors().isDriverAttending())
		{
			this.getNotificationService().removeAllInteractionMechanisms();
			
			this.getNotificationService()
				.addInteractionMechanism("DriverDisplay_VisualText")
				.addInteractionMechanism("Speakers_AuditoryBeep")
				.addInteractionMechanism("SteeringWheel_HapticVibration");
		}
		else
		{
			if (this.getHumanSensors().getFaceStatus() == EFaceStatus.SLEEPING)
			{
				this.getNotificationService().removeAllInteractionMechanisms();
				
				this.getNotificationService()
					.addInteractionMechanism("DriverSeat_HapticVibration")
					.addInteractionMechanism("Speakers_AuditorySound")
					.addInteractionMechanism("SteeringWheel_HapticVibration");
			}
			
			if (this.getHumanSensors().getFaceStatus() == EFaceStatus.DISTRACTED)
			{
				this.getNotificationService().removeAllInteractionMechanisms();
				
				this.getNotificationService()
					.addInteractionMechanism("DriverDisplay_VisualText")
					.addInteractionMechanism("Speakers_AuditoryBeep")
					.addInteractionMechanism("SteeringWheel_HapticVibration");
			}
		}
		
		// INTERACT-2
		if (this.getHumanSensors().areTheHandsOnTheWheel())
		{
			this.getNotificationService().addInteractionMechanism("SteeringWheel_HapticVibration");
		}
		else
		{
			this.getNotificationService()
				.removeInteractionMechanism("SteeringWheel_HapticVibration")
				.addInteractionMechanism("DriverSeat_HapticVibration");
		}
		
		// INTERACT-3
		if (this.getHumanSensors().isDriverSeatOccupied())
		{
			this.getNotificationService()
				.addInteractionMechanism("DriverDisplay_VisualText")
				.addInteractionMechanism("DriverSeat_HapticVibration");
		}
		else
		{
			this.getNotificationService()
				.removeInteractionMechanism("DriverDisplay_VisualText")
				.removeInteractionMechanism("DriverDisplay_VisualIcon")
				.removeInteractionMechanism("DriverSeat_HapticVibration")
				.addInteractionMechanism("Speakers_AuditorySound")
				.addInteractionMechanism("DashboardDisplay_VisualIcon");
		}
		
		// ADS-2
		if (!this.isWorking())
		{
			this.debugMessage("General fail. Changing to manual driving...");
			this.getNotificationService().notify("General fail. Changing to manual driving...");

			this.changeToL0Driving();
			
			return this;
		}
		
		// ADS-L3_7.
		IFallbackPlan fallbackPlan = this.getFallbackPlan();
		boolean isEmergencyPlanSet = fallbackPlan == null ? false : fallbackPlan.getClass().getName().equals(EmergencyFallbackPlan.class.getName());
		
		// Si est� funcionando el plan de emergencia, intentamos cambiar al otro si el tipo de via es el correcto.
		if (isEmergencyPlanSet && 
				(this.getRoadSensor().getRoadType() == ERoadType.STD_ROAD || this.getRoadSensor().getRoadType() == ERoadType.HIGHWAY))
		{
			// Comprobamos primero si los sensores necesarios est�n funcionando.
			if(this.getRightLineSensor().isWorking() && this.getRightDistanceSensor().isWorking())
			{
				this.debugMessage("Changing to Park in the Road Fallback plan.");

				this.setFallbackPlan("ParkInTheRoadShoulderFallbackPlan");
			}
		}
		
		// Si el plan de emergencia es el de aparcar en la cuneta, comprobamos si los sensores est�n funcionando, y si no
		// lo est�n haciendo, cambiamos al plan de emergencia.
		if (!isEmergencyPlanSet)
		{
			if(!this.getRightLineSensor().isWorking() || !this.getRightDistanceSensor().isWorking())
			{
				this.debugMessage("Changing to Emergency plan.");

				this.setFallbackPlan("EmergencyFallbackPlan");
			}
		}
		
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
		
		// ADS_L3-4-2.
		if ( this.getRoadSensor().getRoadType() == ERoadType.CITY) {
			this.debugMessage("Changing to L3 City Chauffer...");
			this.getNotificationService().notify("Changing to L3 City Chauffer...");
			
			this.changeToL3CityDriving();
			
			return this;	
		}
		
		// ADS_L3-6.
		if (!this.getHumanSensors().isWorking())
		{
			if (this.getHumanSensors().isDriverAttending())
			{
				this.debugMessage("Changing to L2 Driving due to a fail in human sensors ...");
				this.getNotificationService().notify("Changing to L2 Driving due to a fail in human sensors ...");
				
				this.changeToL2Driving();
			}
			else
			{
				this.debugMessage("Activating the Fallback Plan due to a fail in human sensors ...");
				this.activateTheFallbackPlan();
			}
			
			return this;
		}
				
		if (!this.getLeftLineSensor().isWorking() || !this.getRightLineSensor().isWorking())
		{
			if (this.getHumanSensors().isDriverAttending())
			{
				this.debugMessage("Take over due to a fail in line sensor ...");
				this.getNotificationService().notify("Take over due to a fail in line sensor ...");
				
				this.performTheTakeOver();
			}
			else
			{
				this.debugMessage("Activating the Fallback Plan due to a fail in line sensor ...");
				this.activateTheFallbackPlan();
			}
			
			return this;
		}
		
		if (!this.getRoadSensor().isWorking())
		{
			if (this.getHumanSensors().isDriverAttending())
			{
				this.debugMessage("Take over due to a fail in road sensor ...");
				this.getNotificationService().notify("Take over due to a fail in road sensor ...");
				
				this.performTheTakeOver();
			}
			else
			{
				this.debugMessage("Activating the Fallback Plan due to a fail in road sensor ...");
				this.activateTheFallbackPlan();
			}
			
			return this;
		}
		
		// Quiz�s cada sensor pueda monitorizarse de forma individual.
		if (!this.getFrontDistanceSensor().isWorking() || !this.getRearDistanceSensor().isWorking()
			|| !this.getRightDistanceSensor().isWorking() || !this.getLeftDistanceSensor().isWorking())
		{
			boolean areLIDARSensors = this.getFrontDistanceSensor().getName().contains("LIDAR");
			
			// No hay un sensor mejor para usar, ya que si se est� usando este significa que los otros
			// tambi�n est�n rotos. Por tanto, tenemos que realizar un Take Over o el Fallback plan seg�n
			// la atenci�n del conductor.
			if (areLIDARSensors)
			{
				if (this.getHumanSensors().isDriverAttending())
				{
					this.debugMessage("Take over due to a fail in distance sensor ...");
					this.getNotificationService().notify("Take over due to a fail in distance sensor ...");
					
					this.performTheTakeOver();
				}
				else
				{
					this.debugMessage("Activating the Fallback Plan due to a fail in distance sensor ...");
					this.activateTheFallbackPlan();
				}
			}
			else
			{
				// Comprobamos si los sensores LIDAR est�n operativos.
				IDistanceSensor LIDAR_FrontDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-FrontDistanceSensor)");
				IDistanceSensor LIDAR_RearDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-RearDistanceSensor)");
				IDistanceSensor LIDAR_RightDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-RightDistanceSensor)");
				IDistanceSensor LIDAR_LeftDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-LeftDistanceSensor)");
			
			    boolean isWorkingLIDAR = LIDAR_FrontDistanceSensor.isWorking() && LIDAR_RearDistanceSensor.isWorking() 
		    		&& LIDAR_RightDistanceSensor.isWorking() && LIDAR_LeftDistanceSensor.isWorking();
			    
			    // Reemplazamos los sensores por los LIDAR.
			    if (isWorkingLIDAR)
			    {
					this.debugMessage("Replacing distance sensors by LIDAR...");
					this.getNotificationService().notify("Replacing distance sensors by LIDAR...");
					
			    	this.setFrontDistanceSensor("LIDAR-FrontDistanceSensor");
			    	this.setRearDistanceSensor("LIDAR-RearDistanceSensor");
			    	this.setRightDistanceSensor("LIDAR-RightDistanceSensor");
			    	this.setLeftDistanceSensor("LIDAR-LeftDistanceSensor");
			    }
			    else
			    {
					if (this.getHumanSensors().isDriverAttending())
					{
						this.debugMessage("Take over due to a fail in distance sensor ...");
						this.getNotificationService().notify("Take over due to a fail in distance sensor ...");
						
						this.performTheTakeOver();
					}
					else
					{
						this.debugMessage("Activating the Fallback Plan due to a fail in distance sensor ...");
						this.activateTheFallbackPlan();
					}
			    }
			}
			
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
}
