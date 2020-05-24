package sua.autonomouscar.driving.l1.assisteddriving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.driving.L1_DrivingService;
import sua.autonomouscar.interfaces.IIdentifiable;

public class L1_AssistedDriving extends L1_DrivingService implements IL1_AssistedDriving {
	
	public static final int DEFAULT_LONGITUDINAL_SECURITY_DISTANCE = 10000;
	
	public L1_AssistedDriving(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL1_AssistedDriving.class.getName());
		this.setLongitudinalSecurityDistance(DEFAULT_LONGITUDINAL_SECURITY_DISTANCE); // cms
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
		
		boolean correction_required = false;
		
		// Reducimos la velocidad un poco si detectamos distancia frontal inferior a distancia de seguridad
		if ( this.getLongitudinalSecurityDistance() > this.getFrontDistanceSensor().getDistance() ) {
			this.debugMessage("Font Distance Warning: ‚äº");
			if ( this.notificationService != null )
				this.getNotificationService().notify("Font Distance Warning: ‚äº");
				
		}

		// Control de la direcci√≥n si nos salimos del carril
		if ( this.getLeftLineSensor().isLineDetected() ) {
			correction_required = true;
			this.debugMessage("Left Line Warning: |<");
			if ( this.notificationService != null )
				this.getNotificationService().notify("Left Line Warning: |<");
		}
		
		if ( this.getRightLineSensor().isLineDetected() ) {
			correction_required = true;
			this.debugMessage("Right Line Warning: >|");
			if ( this.notificationService != null )
				this.getNotificationService().notify("Right Line Warning: >|");
		}
		
		// Si todo va bien, indicamos que seguimos como estamos ...
		if ( !correction_required ) {
			this.debugMessage("Monitoring driving parameters. Nothing to warn ...");
		}
		
		
		return this;
	}	
	
	



}
