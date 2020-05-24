package sua.autonomouscar.infrastructure.driving;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.driving.defaultvalues.L2_AdaptiveCruiseControl_DefaultValues;
import sua.autonomouscar.driving.defaultvalues.L3_CityChauffer_DefaultValues;
import sua.autonomouscar.driving.defaultvalues.L3_HighwayChauffer_DefaultValues;
import sua.autonomouscar.driving.defaultvalues.L3_TrafficJamChauffer_DefaultValues;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.IIdentifiable;

public abstract class L3_DrivingService extends L2_DrivingService implements IL3_DrivingService {

	public final static String REFERENCE_SPEED = "reference-speed";
	
	protected String humanSensors = null;
	protected String roadSensor = null;
	protected String fallbackPlan = null;
	
	protected String l2Level = null;
	
	protected int lateralSecurityDistance = 1;
	
	public L3_DrivingService(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IL3_DrivingService.class.getName());
	}
	
	@Override
	public void setHumanSensors(String humanSensors) {
		this.humanSensors = humanSensors;
		return;
	}

	protected IHumanSensors getHumanSensors() {
		return OSGiUtils.getService(context, IHumanSensors.class, String.format("(%s=%s)", IIdentifiable.ID, this.humanSensors));
	}

	@Override
	public void setRoadSensor(String sensor) {
		this.roadSensor = sensor;
	}
	
	protected IRoadSensor getRoadSensor() {
		return OSGiUtils.getService(context, IRoadSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.roadSensor));
	}
	
	@Override
	public void setFallbackPlan(String plan) {
		this.fallbackPlan = plan;
		return;
	}
	
	@Override
	public IFallbackPlan getFallbackPlan() {
		return OSGiUtils.getService(context, IFallbackPlan.class, String.format("(%s=%s)", IIdentifiable.ID, this.fallbackPlan));
	}

	
	@Override
	public void setReferenceSpeed(int speed) {
		this.setProperty(L3_DrivingService.REFERENCE_SPEED, speed);
	}
	
	@Override
	public int getReferenceSpeed() {
		return (int)this.getProperty(L3_DrivingService.REFERENCE_SPEED);
	}

	@Override
	public IL3_DrivingService performTheTakeOver() {
		this.stopDriving();
		this.getNotificationService().notify("Exited Autonomous Mode");
		return this;
	}

	@Override
	public IL3_DrivingService activateTheFallbackPlan() {
		this.stopDriving();
		this.getFallbackPlan().startDriving();

		return this;
	}
	
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
	
	public IL3_DrivingService changeToL3TrafficDriving() {
		// First, stops driving.
		this.stopDriving();

		// Obtains the registered control and configures it.
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		theL3TrafficJamChaufferService.setHumanSensors("HumanSensors");
		theL3TrafficJamChaufferService.setRoadSensor("RoadSensor");
		theL3TrafficJamChaufferService.setEngine("Engine");
		theL3TrafficJamChaufferService.setSteering("Steering");
		theL3TrafficJamChaufferService.setFrontDistanceSensor("FrontDistanceSensor");
		theL3TrafficJamChaufferService.setRearDistanceSensor("RearDistanceSensor");
		theL3TrafficJamChaufferService.setRightDistanceSensor("RightDistanceSensor");
		theL3TrafficJamChaufferService.setLeftDistanceSensor("LeftDistanceSensor");
		theL3TrafficJamChaufferService.setRightLineSensor("RightLineSensor");
		theL3TrafficJamChaufferService.setLeftLineSensor("LeftLineSensor");
		
		theL3TrafficJamChaufferService.setReferenceSpeed(L3_TrafficJamChauffer_DefaultValues.DEFAULT_REFERENCE_SPEED);
		theL3TrafficJamChaufferService.setLongitudinalSecurityDistance(L3_TrafficJamChauffer_DefaultValues.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);
		theL3TrafficJamChaufferService.setLateralSecurityDistance(L3_TrafficJamChauffer_DefaultValues.DEFAULT_LATERAL_SECURITY_DISTANCE);

		theL3TrafficJamChaufferService.setNotificationService("NotificationService");		
		
		theL3TrafficJamChaufferService.setFallbackPlan("EmergencyFallbackPlan");	
		
		// Starts driving with L3 level.
		theL3TrafficJamChaufferService.startDriving();
		
		return this;
	}
	
	public IL3_DrivingService changeToL3CityDriving()
	{
		// First, stops driving.
		this.stopDriving();
		
		// Obtains the registered control and configures it.
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);
		theL3CityChaufferService.setHumanSensors("HumanSensors");
		theL3CityChaufferService.setRoadSensor("RoadSensor");
		theL3CityChaufferService.setEngine("Engine");
		theL3CityChaufferService.setSteering("Steering");
		theL3CityChaufferService.setFrontDistanceSensor("FrontDistanceSensor");
		theL3CityChaufferService.setRearDistanceSensor("RearDistanceSensor");
		theL3CityChaufferService.setRightDistanceSensor("RightDistanceSensor");
		theL3CityChaufferService.setLeftDistanceSensor("LeftDistanceSensor");
		theL3CityChaufferService.setRightLineSensor("RightLineSensor");
		theL3CityChaufferService.setLeftLineSensor("LeftLineSensor");
		
		theL3CityChaufferService.setReferenceSpeed(L3_CityChauffer_DefaultValues.DEFAULT_REFERENCE_SPEED);
		theL3CityChaufferService.setLongitudinalSecurityDistance(L3_CityChauffer_DefaultValues.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);
		theL3CityChaufferService.setLateralSecurityDistance(L3_CityChauffer_DefaultValues.DEFAULT_LATERAL_SECURITY_DISTANCE);

		theL3CityChaufferService.setNotificationService("NotificationService");		

		theL3CityChaufferService.setFallbackPlan("EmergencyFallbackPlan");
		
		// Starts driving with L3 level.
		theL3CityChaufferService.startDriving();
		
		return this;
	}
}
