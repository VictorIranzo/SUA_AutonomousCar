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
import sua.autonomouscar.driving.l3.citychauffer.L3_CityChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class L3_CityChaufferConfigurator {
	
	
	public static void configure(BundleContext context) {
		
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
		
		theL3CityChaufferService.setReferenceSpeed(L3_CityChauffer.DEFAULT_REFERENCE_SPEED);
		theL3CityChaufferService.setLongitudinalSecurityDistance(L3_CityChauffer.DEFAULT_LONGITUDINAL_SECURITY_DISTANCE);
		theL3CityChaufferService.setLateralSecurityDistance(L3_CityChauffer.DEFAULT_LATERAL_SECURITY_DISTANCE);

		theL3CityChaufferService.setNotificationService("NotificationService");		

		theL3CityChaufferService.setFallbackPlan("EmergencyFallbackPlan");
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

		theL3HighwayChaufferService.stopDriving();
		theL3TrafficJamChaufferService.stopDriving();
		theL2AdaptiveCruiserControlService.stopDriving();
		theL2LaneKeepingAssistService.stopDriving();
		theL1AssistedDrivingService.stopDriving();
		theL0ManualDrivingService.stopDriving();
		theEmergencyFallbackPlan.stopDriving();
		theParkInTheRoadShoulderFallbackPlan.stopDriving();
		
		theL3CityChaufferService.startDriving();

		
	}
}
