package sua.autonomouscar.simulation.console.commands;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.devices.interfaces.ISpeedometer;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.devices.DistanceSensor;
import sua.autonomouscar.infrastructure.driving.DrivingService;
import sua.autonomouscar.interfaces.EFaceStatus;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.interfaces.IIdentifiable;
import sua.autonomouscar.simulation.IManualSimulatorStepsManager;

public class MyCommandProvider {

	BundleContext context = null;

	public MyCommandProvider(BundleContext context) {
		this.context = context;
	}
	
	public void show() {
		IHumanSensors hs = OSGiUtils.getService(context, IHumanSensors.class);
		if ( hs != null ) {
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println("|                    DRIVER");
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println(String.format("| Hands on Wheel: %s", hs.areTheHandsOnTheWheel()));
			System.out.println(String.format("|    Driver Face: %s", hs.getFaceStatus().name()));
			System.out.println(String.format("|    Driver Seat: %s", hs.isDriverSeatOccupied()));
			System.out.println(String.format("|   Copilot Seat: %s", hs.isCopilotSeatOccupied()));
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -\n");
		}
		
		IRoadSensor rs = OSGiUtils.getService(context, IRoadSensor.class);
		if ( rs != null ) {
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println("|                  ROAD INFO");
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println(String.format("|      Road Type: %s", rs.getRoadType()));
			System.out.println(String.format("|    Road Status: %s", rs.getRoadStatus()));
			System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -\n");
		}


		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("|                  CAR INFO");
		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -");

		ISpeedometer ss = OSGiUtils.getService(context, ISpeedometer.class);
		if ( ss != null )
			System.out.println(String.format("|\t   Speed: %s Km/h", ss.getCurrentSpeed()));

		IEngine engine = OSGiUtils.getService(context, IEngine.class);
		if ( engine != null )
			System.out.println(String.format("|\t  Engine: %s rpm", engine.getCurrentRPM()));

		ISteering steering = OSGiUtils.getService(context, ISteering.class);
		if ( steering != null )
			System.out.println(String.format("|\tSteering: %d º", steering.getCurrentAngle()));

		System.out.println("|  - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("|  DRIVING SERVICE");
		IDrivingService drivingService = OSGiUtils.getService(context, IDrivingService.class, String.format("(%s=true)", DrivingService.ACTIVE));
		if ( drivingService == null )
			System.out.println("|\t<none>");
		else {
			System.out.println(String.format("|\t%s", drivingService.getId()));
		
			try {
				System.out.println(String.format("|\t          Reference Speed: %d Km/h", ((IL3_DrivingService)drivingService).getReferenceSpeed()));
			} catch (Exception e) {
			}
			
			try {
				System.out.println(String.format("|\tLongit. Security Distance: %d cms", ((IL1_DrivingService)drivingService).getLongitudinalSecurityDistance()));
			} catch (Exception e) {
			}

			try {
				System.out.println(String.format("|\tLateral Security Distance: %d cms", ((IL2_DrivingService)drivingService).getLateralSecurityDistance()));
			} catch (Exception e) {
			}

		}
		

		System.out.println("|  - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("|  DISTANCES");
		IDistanceSensor frontDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=FrontDistanceSensor)");
		if ( frontDistanceSensor != null ) {
			int distance = frontDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\tFront: %s cms", sDistance));
		}
		IDistanceSensor rearDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=RearDistanceSensor)");
		if ( rearDistanceSensor != null ) {
			int distance = rearDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\t Rear: %s cms", sDistance));
		}
		IDistanceSensor rightDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=RightDistanceSensor)");
		if ( rightDistanceSensor != null ) {
			int distance = rightDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\tRight: %s cms", sDistance));
		}
		IDistanceSensor leftDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LeftDistanceSensor)");
		if ( leftDistanceSensor != null ) {
			int distance = leftDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\t Left: %s cms", sDistance));
		}


		System.out.println("|  - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("|  LIDAR");
		IDistanceSensor LIDAR_FrontDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-FrontDistanceSensor)");
		if ( LIDAR_FrontDistanceSensor != null ) {
			int distance = LIDAR_FrontDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\tFront: %s cms", sDistance));
		}
		IDistanceSensor LIDAR_RearDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-RearDistanceSensor)");
		if ( LIDAR_RearDistanceSensor != null ) {
			int distance = LIDAR_RearDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\t Rear: %s cms", sDistance));
		}		IDistanceSensor LIDAR_RightDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-RightDistanceSensor)");
		if ( LIDAR_RightDistanceSensor != null ) {
			int distance = LIDAR_RightDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\tRight: %s cms", sDistance));
		}
		IDistanceSensor LIDAR_LeftDistanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, "(" + IIdentifiable.ID + "=LIDAR-LeftDistanceSensor)");
		if ( LIDAR_LeftDistanceSensor != null ) {
			int distance = LIDAR_LeftDistanceSensor.getDistance();
			String sDistance = (distance != DistanceSensor.MAX_DISTANCE ? String.valueOf(distance) : "∞");
			System.out.println(String.format("|\t Left: %s cms", sDistance));
		}

		System.out.println("|  - - - - - - - - - - - - - - - - - - - - - -");
		System.out.println("|  LINE SENSORS");
		ILineSensor rightLineSensor = OSGiUtils.getService(context, ILineSensor.class, "(" + IIdentifiable.ID + "=RightLineSensor)");
		if ( rightLineSensor != null ) {
			System.out.println(String.format("|\tRight: %b", rightLineSensor.isLineDetected()));
		}
		ILineSensor leftLineSensor = OSGiUtils.getService(context, ILineSensor.class, "(" + IIdentifiable.ID + "=LeftLineSensor)");
		if ( leftLineSensor != null ) {
			System.out.println(String.format("|\t Left: %b", leftLineSensor.isLineDetected()));
		}


		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - -\n");

	}
	
	public void configure() {
		L0_ManualDrivingConfigurator.configure(context);
		L1_AssistedDrivingConfigurator.configure(context);
		L2_AdaptiveCruiseControlConfigurator.configure(context);
		L2_LaneKeepingAssistConfigurator.configure(context);
		L3_HighwayChaufferConfigurator.configure(context);
		L3_TrafficJamChaufferConfigurator.configure(context);
		L3_CityChaufferConfigurator.configure(context);
		EmergencyFallbackPlanConfigurator.configure(context);
		ParkInTheRoadShoulderFallbackPlanConfigurator.configure(context);
		NotificationServiceConfigurator.configure(context);
	}
	
	public void driver(String property, String s) {
		IHumanSensors sensor = OSGiUtils.getService(context, IHumanSensors.class);
		if ( sensor == null )
			return;
		if ( property.equalsIgnoreCase("face") ) {
			if ( s.equalsIgnoreCase("looking-forward") )
				sensor.setFaceStatus(EFaceStatus.LOOKING_FORWARD);
			else if ( s.equalsIgnoreCase("distracted") )
				sensor.setFaceStatus(EFaceStatus.DISTRACTED);
			else if ( s.equalsIgnoreCase("sleeping") )
				sensor.setFaceStatus(EFaceStatus.SLEEPING);
			
		} else if ( property.equalsIgnoreCase("hands") ) {
			if ( s.equalsIgnoreCase("on-wheel") ) {
				sensor.setTheHandsOnTheSteeringWheel(true);
			} else if ( s.equalsIgnoreCase("off-wheel") ) {
				sensor.setTheHandsOnTheSteeringWheel(false);
			}
		}
	}
		
	public void road(String property, String s) {
		IRoadSensor rs = OSGiUtils.getService(context, IRoadSensor.class);
		if ( rs == null )
			return;
		if ( property.equalsIgnoreCase("type") ) {
			if ( s.equalsIgnoreCase("std") )
				rs.setRoadType(ERoadType.STD_ROAD);
			else if ( s.equalsIgnoreCase("city") )
				rs.setRoadType(ERoadType.CITY);
			else if ( s.equalsIgnoreCase("highway") )
				rs.setRoadType(ERoadType.HIGHWAY);
			else if ( s.equalsIgnoreCase("off-road") )
				rs.setRoadType(ERoadType.OFF_ROAD);
			
		} else if ( property.equalsIgnoreCase("status") ) {
			if ( s.equalsIgnoreCase("fluid") ) {
				rs.setRoadStatus(ERoadStatus.FLUID);
			} else if ( s.equalsIgnoreCase("jam") ) {
				rs.setRoadStatus(ERoadStatus.JAM);
			} else if ( s.equalsIgnoreCase("collapsed") ) {
				rs.setRoadStatus(ERoadStatus.COLLAPSED);
			}
		}
	}

	public void seat(String s, boolean value) {
		IHumanSensors sensor = OSGiUtils.getService(context, IHumanSensors.class);
		if ( sensor == null )
			return;
		if ( s.equalsIgnoreCase("driver") )
			sensor.setDriverSeatOccupancy(value);
		else if ( s.equalsIgnoreCase("copilot") )
			sensor.setDriverSeatOccupancy(value);
	}
	
	public void driving(String function) {
		
		if ( function.equalsIgnoreCase("l0") ) {
			
			L0_ManualDrivingConfigurator.start(context);
			return;
			
		} else if ( function.equalsIgnoreCase("l1") ) {
			
			L1_AssistedDrivingConfigurator.start(context);
			return;

		} else if ( function.equalsIgnoreCase("l2") ) {
			
			if ( Math.random() > 0.5 )
				L2_AdaptiveCruiseControlConfigurator.start(context);
			else
				L2_LaneKeepingAssistConfigurator.start(context);
			return;

	
		} else if ( function.equalsIgnoreCase("l3") ) {
			
			IRoadSensor roadSensor = OSGiUtils.getService(context, IRoadSensor.class);
			if (roadSensor == null) {
				System.out.println("No RoadSensor found! Enabling L2 Autonomous Driving ...");
				this.driving("l2");
				return;
			}
			
			switch (roadSensor.getRoadType()) {
				case HIGHWAY:
					if ( roadSensor.getRoadStatus() == ERoadStatus.FLUID ) {
						L3_HighwayChaufferConfigurator.start(context);
						return;
	
					} else {
						L3_TrafficJamChaufferConfigurator.start(context);
						return;
					}
					
				case CITY:
						L3_CityChaufferConfigurator.start(context);
					return;
	
				default:
					break;

			}
			
			System.out.println("Cannot enable L3 Autonomous Driving! Enabling L2 Autonomous Driving ...");
			this.driving("l2");
			return;
			
			
			
			
		}
		
		
	}

	public void engine(String s, int rpm) {
		IEngine engine = OSGiUtils.getService(context, IEngine.class);
		if ( engine == null )
			return;
		if ( s.equalsIgnoreCase("rpm") )
			engine.setRPM(rpm);
		else if ( s.equalsIgnoreCase("accelerate") )
			engine.accelerate(rpm);
		else if ( s.equalsIgnoreCase("decelerate") )
			engine.decelerate(rpm);
	}

	public void steering(String s, int angle) {
		ISteering steering = OSGiUtils.getService(context, ISteering.class);
		if ( steering == null )
			return;
		else if ( s.equalsIgnoreCase("right") )
			steering.rotateRight(angle);
		else if ( s.equalsIgnoreCase("left") )
			steering.rotateLeft(angle);
	}
	
	public void line(String line, boolean value) {
		// line = Left or Right (case insensitive)
		String sensorId = null;
		if ( line.equalsIgnoreCase("left") )
			sensorId = "LeftLineSensor";
		else if ( line.equalsIgnoreCase("right") )
			sensorId = "RightLineSensor";
		
		ILineSensor sensor = OSGiUtils.getService(context, ILineSensor.class, String.format("(id=%s)", sensorId));
		if ( sensor != null )
			sensor.setLineDetected(value);
	}
	
	public void distance(String l, int distance) {
		// l = Front | Rear | Left | Right (case insensitive)
		String sensorId = null;
		if ( l.equalsIgnoreCase("front") )
			sensorId = "FrontDistanceSensor";
		else if ( l.equalsIgnoreCase("rear") )
			sensorId = "RearDistanceSensor";
		else if ( l.equalsIgnoreCase("right") )
			sensorId = "RightDistanceSensor";
		else if ( l.equalsIgnoreCase("left") )
			sensorId = "LeftDistanceSensor";

		IDistanceSensor sensor = OSGiUtils.getService(context, IDistanceSensor.class, String.format("(id=%s)", sensorId));
		if ( sensor != null )
			sensor.setDistance(distance);
	}

	public void lidar(String l, int distance) {
		// l = Front | Rear | Left | Right (case insensitive)
		String sensorId = null;
		if ( l.equalsIgnoreCase("front") )
			sensorId = "LIDAR-FrontDistanceSensor";
		else if ( l.equalsIgnoreCase("rear") )
			sensorId = "LIDAR-RearDistanceSensor";
		else if ( l.equalsIgnoreCase("right") )
			sensorId = "LIDAR-RightDistanceSensor";
		else if ( l.equalsIgnoreCase("left") )
			sensorId = "LIDAR-LeftDistanceSensor";

		IDistanceSensor sensor = OSGiUtils.getService(context, IDistanceSensor.class, String.format("(id=%s)", sensorId));
		if ( sensor != null )
			sensor.setDistance(distance);
	}
	
	public void next() {
		IManualSimulatorStepsManager manager = OSGiUtils.getService(context, IManualSimulatorStepsManager.class);
		if ( manager != null )
			manager.next();
	}
	
	public void n() {
		this.next();
	}

}
