package sua.autonomouscar.infrastructure.devices;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IFaceMonitor;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.EFaceStatus;

public class DriverFaceMonitor extends Thing implements IFaceMonitor {
	
	public final static String FACE_STATUS = "face-status";

	public DriverFaceMonitor(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IFaceMonitor.class.getName());
		this.setFaceStatus(EFaceStatus.UNKNOWN);
	}

	@Override
	public EFaceStatus getFaceStatus() {
		return (EFaceStatus) this.getProperty(DriverFaceMonitor.FACE_STATUS);
	}
	
	
	public IFaceMonitor setFaceStatus(EFaceStatus status) {
		this.setProperty(DriverFaceMonitor.FACE_STATUS, status);
		return this;
	}

}
