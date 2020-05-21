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
import sua.autonomouscar.infrastructure.OSGiUtils;

public class L0_ManualDrivingConfigurator {
	
	
	public static void configure(BundleContext context) {
		
		
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
		theL3HighwayChaufferService.stopDriving();
		theL3TrafficJamChaufferService.stopDriving();
		theL2AdaptiveCruiserControlService.stopDriving();
		theL2LaneKeepingAssistService.stopDriving();
		theL1AssistedDrivingService.stopDriving();
		theEmergencyFallbackPlan.stopDriving();
		theParkInTheRoadShoulderFallbackPlan.stopDriving();
		
		theL0ManualDrivingService.startDriving();
		
	}

}
