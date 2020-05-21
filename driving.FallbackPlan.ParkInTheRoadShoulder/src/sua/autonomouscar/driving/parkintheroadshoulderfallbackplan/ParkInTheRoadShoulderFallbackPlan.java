package sua.autonomouscar.driving.parkintheroadshoulderfallbackplan;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.ISpeedometer;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IParkInTheRoadShoulderFallbackPlan;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.devices.Steering;
import sua.autonomouscar.infrastructure.driving.FallbackPlan;
import sua.autonomouscar.interfaces.IIdentifiable;

public class ParkInTheRoadShoulderFallbackPlan extends FallbackPlan implements IParkInTheRoadShoulderFallbackPlan {
	
	public static final int MY_FINE_ACCELERATION_RPM = 5;
	public static final int MY_SMOOTH_ACCELERATION_RPM = 50;
	public static final int MY_MEDIUM_ACCELERATION_RPM = 100;
	public static final int MY_HIGH_ACCELERATION_RPM = 200;
	public static final int MY_AGGRESSIVE_ACCELERATION_RPM = 300;
	
	public static final int ANCHO_CARRIL = 3;

	protected String rightDistanceSensor = null;
	protected String rightLineSensor = null;
	
	protected int phase = 1;
		
	public ParkInTheRoadShoulderFallbackPlan(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IParkInTheRoadShoulderFallbackPlan.class.getName());
	}
	
	
	@Override
	public void setRightDistanceSensor(String sensor) {
		this.rightDistanceSensor = sensor;
		return;
	}
	
	protected IDistanceSensor getRightDistanceSensor() {
		return OSGiUtils.getService(context, IDistanceSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.rightDistanceSensor));
	}

		
	@Override
	public void setRightLineSensor(String sensor) {
		this.rightLineSensor = sensor;
		return;
	}
	
	protected ILineSensor getRightLineSensor() {
		return OSGiUtils.getService(context, ILineSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.rightLineSensor));
	}

	
	
	@Override
	public IDrivingService performTheDrivingFunction() {
		
		// Aparcamos en la cuneta (Road Shoulder). Asumimos que vamos por el carril derecho de la vía ...
		
		// Fase 1 : nos movemos a la derecha, si no hay obstáculo, hasta cruzar la línea de la calzada
		if ( this.phase == 1 ) {
			if ( this.getRightDistanceSensor().getDistance() > ANCHO_CARRIL ) {
				this.debugMessage("Turning to the right ...");
				this.getSteering().rotateRight(Steering.MEDIUM_CORRECTION_ANGLE);
				this.phase = 2;
				return this;
			}
		}
		
		// Fase 2 : esperamos a que el sensor de carril derecho indique que estamos cruzando la línea ...
		if ( this.phase == 2 ) {
			this.debugMessage("Waiting to detect the right line");
			if ( this.getRightLineSensor().isLineDetected() ) {
				this.debugMessage("Line Detected ...");
				this.phase = 3;
				return this;
			}
		}
		
		// Fase 3 : cuando hemos cruzado el carril, nos preparamos para realizar la parada ...
		if ( this.phase == 3 ) {
			this.debugMessage("Waiting to cross the line ...");
			if ( !this.getRightLineSensor().isLineDetected() ) {
				this.debugMessage("Reached the Road Shoulder");
				this.debugMessage("Centering the steering");
				this.getSteering().center();
				this.phase = 4;
				return this;
			}
		}
		
		// Fase 4 : detenemos el vehiculo
		if ( this.phase == 4 ) {
			
			ISpeedometer speedometer = OSGiUtils.getService(context, ISpeedometer.class);
			int currentSpeed = speedometer.getCurrentSpeed();
			this.debugMessage(String.format("Speed: %d Km/h" , currentSpeed));

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

			} else {
				return this.stopDriving();
			}
			return this;
		
		}
		
		

		
		return this;
	}

	@Override
	public IDrivingService stopTheDrivingFunction() {
		this.debugMessage("Parked in the Road Shoulder");
		return this;
	}
	
	



}
