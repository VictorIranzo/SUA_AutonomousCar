package sua.autonomouscar.speedometer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.infrastructure.devices.Speedometer;

public class SpeedChangeListener implements ServiceListener {
	
	protected BundleContext context = null;
	protected Speedometer speedodometer = null;
	
	public SpeedChangeListener(BundleContext context, Speedometer speedometer) {
		this.context = context;
		this.speedodometer = speedometer;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
	
		IEngine engine = (IEngine)context.getService(event.getServiceReference());
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
		case ServiceEvent.MODIFIED:
			this.speedodometer.updateSpeed(engine.getCurrentRPM());
			break;

		case ServiceEvent.UNREGISTERING:
		case ServiceEvent.MODIFIED_ENDMATCH:
			this.speedodometer.updateSpeed(0);
			break;
		default:
			break;
		}
	}
	
	

}
