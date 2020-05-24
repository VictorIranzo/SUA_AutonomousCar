package sua.autonomouscar.driving.l2.acc;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.driving.defaultvalues.L2_AdaptiveCruiseControl_DefaultValues;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.devices.Engine;
import sua.autonomouscar.infrastructure.driving.L2_DrivingService;
import sua.autonomouscar.interfaces.IIdentifiable;

public class L2_AdaptiveCruiseControl extends L2_DrivingService implements IL2_AdaptiveCruiseControl {	
	public L2_AdaptiveCruiseControl(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL2_AdaptiveCruiseControl.class.getName());
		this.setLongitudinalSecurityDistance(L2_AdaptiveCruiseControl_DefaultValues.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE); // cms
	}
	
	@Override
	public IDrivingService performTheDrivingFunction() {
		// ADS-1
		if (this.getFrontDistanceSensor().getClass().getName().contains("LIDAR"))
		{
			// Comprobamos si el sensor de distancia dedicados est·n disponibles, para emplearlos.
			IDistanceSensor FrontDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=FrontDistanceSensor)");
	
		    boolean isWorkingDistanceSensor = FrontDistanceSensor.isWorking();
		    
		    // Seg˙n la especificaciÛn, solo se necesita el sensor frontal.
		    if (isWorkingDistanceSensor)
		    {
		    	this.setFrontDistanceSensor("FrontDistanceSensor");
		    }
		}	
		
		boolean correction_performed = false;
		
		// Reducimos la velocidad un poco si detectamos distancia frontal inferior a distancia de seguridad
		if ( this.getLongitudinalSecurityDistance() > this.getFrontDistanceSensor().getDistance() ) {
			
				this.getEngine().decelerate(Engine.MEDIUM_ACCELERATION_RPM);
				correction_performed = true;
				this.debugMessage("Font Distance Warning: ‚äº");
				this.getNotificationService().notify("Font Distance Warning: Braking ...");
		}
		
		// Si todo va bien, indicamos que seguimos como estamos ...
		if ( !correction_performed ) {
			this.debugMessage("Monitoring driving parameters. Nothing to warn ...");
		}
				
		return this;
	}	
	
	



}
