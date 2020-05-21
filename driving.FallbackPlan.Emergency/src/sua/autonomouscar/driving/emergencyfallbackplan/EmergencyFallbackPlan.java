package sua.autonomouscar.driving.emergencyfallbackplan;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.ISpeedometer;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IEmergencyFallbackPlan;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.driving.FallbackPlan;

public class EmergencyFallbackPlan extends FallbackPlan implements IEmergencyFallbackPlan {

	public static final int MY_FINE_ACCELERATION_RPM = 5;
	public static final int MY_SMOOTH_ACCELERATION_RPM = 50;
	public static final int MY_MEDIUM_ACCELERATION_RPM = 100;
	public static final int MY_HIGH_ACCELERATION_RPM = 200;
	public static final int MY_AGGRESSIVE_ACCELERATION_RPM = 300;

	public EmergencyFallbackPlan(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IEmergencyFallbackPlan.class.getName());
	}
	
	
	
	@Override
	public IDrivingService performTheDrivingFunction() {
		
		// Frenamos gradualmente hasta detenernos ...
		// No podemos hacer mucho más, ya que no tenemos sensores de carril, ni de distancia
		
		// Activamos las luces de emergencia para advertir al resto de vehículos de la carretera ...
		// this.emergencyLights.activate()
		
		// ... centramos la dirección para detener el vehículo en el carril ...
		this.getSteering().center();

		ISpeedometer speedometer = OSGiUtils.getService(context, ISpeedometer.class);
		int currentSpeed = speedometer.getCurrentSpeed();

		if ( currentSpeed > 0 ) {
			int diffSpeed = currentSpeed;
			
			int rpmCorrection = MY_FINE_ACCELERATION_RPM;
			String rpmAppliedCorrection = "fine";
			if ( Math.abs(diffSpeed) > 30 ) { rpmCorrection = MY_AGGRESSIVE_ACCELERATION_RPM; rpmAppliedCorrection = "aggressive"; }
			else if ( Math.abs(diffSpeed) > 15 ) { rpmCorrection = MY_HIGH_ACCELERATION_RPM; rpmAppliedCorrection = "high"; }
			else if ( Math.abs(diffSpeed) > 5 ) { rpmCorrection = MY_MEDIUM_ACCELERATION_RPM; rpmAppliedCorrection = "medium"; }
			else if ( Math.abs(diffSpeed) > 1 ) { rpmCorrection = MY_SMOOTH_ACCELERATION_RPM; rpmAppliedCorrection = "smooth"; }
			
			this.getEngine().decelerate(rpmCorrection);
			this.debugMessage(String.format("Decelerating (%s) ...", rpmAppliedCorrection));
		}
		else
			this.stopDriving();


		return this;
	}



	@Override
	public IDrivingService stopTheDrivingFunction() {

		this.debugMessage("Stopped with the Emergency Lights engaged");
		return this;
	}

	



}
