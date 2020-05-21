package sua.autonomouscar.simulation.console.commands;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.interfaces.IEmergencyFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL2_LaneKeepingAssist;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.driving.interfaces.IParkInTheRoadShoulderFallbackPlan;
import sua.autonomouscar.driving.l3.highwaychauffer.L3_HighwayChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class L3_HighwayChaufferConfigurator {
	
	
	public static void configure(BundleContext context) {
		
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
		
		theL3HighwayChaufferService.setReferenceSpeed(L3_HighwayChauffer.DEFAULT_REFERENCE_SPEED);
		theL3HighwayChaufferService.setLongitudinalSecurityDistance(L3_HighwayChauffer.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);
		theL3HighwayChaufferService.setLateralSecurityDistance(L3_HighwayChauffer.DEFAULT_LATERAL_SECURITY_DISTANCE);

		theL3HighwayChaufferService.setNotificationService("NotificationService");		

		theL3HighwayChaufferService.setFallbackPlan("ParkInTheRoadShoulderFallbackPlan");
	}

	
	public static void start(BundleContext context) {
		
		IL0_ManualDriving theL0ManualDrivingService = OSGiUtils.getService(context, IL0_ManualDriving.class);
		IL1_AssistedDriving theL1AssistedDrivingService = OSGiUtils.getService(context, IL1_AssistedDriving.class);
		IL2_LaneKeepingAssist theL2LaneKeepingAssistService = OSGiUtils.getService(context, IL2_LaneKeepingAssist.class);
		IL2_AdaptiveCruiseControl theL2AdaptiveCruiserControlService = OSGiUtils.getService(context, IL2_AdaptiveCruiseControl.class);
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		IEmergencyFallbackPlan theEmergencyFallbackPlan = OSGiUtils.getService(context, IEmergencyFallbackPlan.class);
		IParkInTheRoadShoulderFallbackPlan theParkInTheRoadShoulderFallbackPlan = OSGiUtils.getService(context, IParkInTheRoadShoulderFallbackPlan.class);

		theL3CityChaufferService.stopDriving();
		theL3TrafficJamChaufferService.stopDriving();
		theL2AdaptiveCruiserControlService.stopDriving();
		theL2LaneKeepingAssistService.stopDriving();
		theL1AssistedDrivingService.stopDriving();
		theL0ManualDrivingService.stopDriving();
		theEmergencyFallbackPlan.stopDriving();
		theParkInTheRoadShoulderFallbackPlan.stopDriving();
		
		theL3HighwayChaufferService.startDriving();
	}
}
