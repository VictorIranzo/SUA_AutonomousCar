package sua.autonomouscar.simulation.console.commands;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.driving.interfaces.IEmergencyFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.driving.interfaces.IParkInTheRoadShoulderFallbackPlan;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class ParkInTheRoadShoulderFallbackPlanConfigurator {
	
	
	public static void configure(BundleContext context) {
		
		IParkInTheRoadShoulderFallbackPlan theFallbackPlan = OSGiUtils.getService(context, IParkInTheRoadShoulderFallbackPlan.class);		
		theFallbackPlan.setEngine("Engine");
		theFallbackPlan.setSteering("Steering");
		theFallbackPlan.setNotificationService("NotificationService");		
		theFallbackPlan.setRightLineSensor("RightLineSensor");
		theFallbackPlan.setRightDistanceSensor("RightDistanceSensor");
	}

	
	
	public static void start(BundleContext context) {
		
		IL0_ManualDriving theL1ManualDrivingService = OSGiUtils.getService(context, IL0_ManualDriving.class);
		IL1_AssistedDriving theL2AssistedDrivingService = OSGiUtils.getService(context, IL1_AssistedDriving.class);
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		IEmergencyFallbackPlan theEmergencyFallbackPlan = OSGiUtils.getService(context, IEmergencyFallbackPlan.class);
		IParkInTheRoadShoulderFallbackPlan theParkInTheRoadShoulderFallbackPlan = OSGiUtils.getService(context, IParkInTheRoadShoulderFallbackPlan.class);

		theL3CityChaufferService.stopDriving();
		theL3HighwayChaufferService.stopDriving();
		theL3TrafficJamChaufferService.stopDriving();
		theL2AssistedDrivingService.stopDriving();
		theL1ManualDrivingService.stopDriving();
		theEmergencyFallbackPlan.stopDriving();
		
		theParkInTheRoadShoulderFallbackPlan.startDriving();
		
	}
}
