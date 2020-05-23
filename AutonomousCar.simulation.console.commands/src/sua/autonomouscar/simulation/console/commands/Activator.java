package sua.autonomouscar.simulation.console.commands;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected ServiceRegistration<?> commandProvReg = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "AutonomousCar");
		props.put("osgi.command.function", new String[] { 
				
				//
				// CONFIGURACIÃ“N 
				//
				
					//  configure : realiza una configuraciÃ³n inicial de servicios y 
					//    dispositivos (de prueba)
					//
					//   Modo uso
					//		configure
					//
					"configure", 					
					
					//   show : muestra la configuraciÃ³n actual de conducciÃ³n
					//
					//   Modo uso
					//		configure
					//
					"show", 						
				
					//
				// SIMULACIÃ“N LECTURAS DE SENSORES
				//
					//  line : sensores de carril (LineSensor)
					//
					//	Modo uso
					//		line [ right | left ]  [ true | false ]
					//
					//  Ejemplo: detecciÃ³n de lÃ­nea derecha de carril
					//		line right true
					//
					"line", 
					
					//  distance : sensores de distancia (DistanceSensor)
					//
					//  Modo uso
					//		distance [ front | rear | left | right ] valor-distancia
					//
					//  Ejemplo: distancia frontal de 150 m (15000 cms)
					//		distance front 15000
					//
					"distance", 
					
					//  lidar : sensores de distancia en LIDAR (LIDAR-DistanceSensor)
					//
					//  Modo uso
					//		lidar [ front | rear | left | right ] valor-distancia
					//
					//  Ejemplo: distancia a obstÃ¡culo en lateral izquierdo de 70 cms
					//		lidar left 70
					//
					"lidar",	
				

				//	
				// PARÃ�METROS DE CONTEXTO
				//	
					//  driver : situaciÃ³n del conductor. Permite varios usos para
					//    indicar la situaciÃ³n de la cara (face) y las manos en el
					//    volante
					//
					//  Modo uso
					//		driver face [ looking_forward | distracted | sleeping ]
					//		driver hands [ on-wheel | off-wheel ]
					//
					//  Ejemplo: conductor distraÃ­do
					//		driver face distracted
					//
					//  Ejemplo: conductor distraÃ­do
					//		driver hands on-wheel
					//
					"driver", 
					
					//  seat : sensor de asiento de conductor y copiloto
					//  Modo uso
					//		seat [ driver | copilot ] [ true | false ]
					//
					//  Ejemplo: asiento del conductor ocupado
					//		seat driver true
					//
					"seat", 
					
					// road : sensor de carretera. Permite indicar tanto el tipo
					//   como el estado
					//
					//  Modo uso
					//		road type [ std | highway | off-road | city ]
					//		road status [ fluid | jam |Â collapsed ]
					//
					//  Ejemplo: circulamos por ciudad
					//		road type city
					//
					//  Ejemplo: hay atasco
					//		road status jam
					"road",
					
				//	
				// CONTROL MANUAL DEL VEHÃ�CULO
				//	
				
					// engine : control de las revoluciones (rpm) del motor
					//
					//  Modo uso
					//		engine [ rpm | accelerate | decelerate ] numero-rpm
					//
					//  Ejemplo: Poner las revoluciones a 2500 rpm
					//		engine rpm 2500
					//
					//  Ejemplo: Aceleramos 1000 rpm
					//		engine accelerate 1000
					//
					"engine", 
					
					// steering : control de direcciÃ³n, a izquierda o derecha
					//
					//  Modo uso
					//		steering [ left | right ] grados-giro
					//
					//  Ejemplo: Giramos 20 grados a la derecha
					//		steering right 20
					//
					"steering", 			// controlar manualmente las funciones primarias de conducciÃ³n
					
					
					
				//	
				// FUNCIONES DE CONDUCCIÃ“N
				//	

					// driving : activa un nivel de conducciÃ³n autÃ³noma
					//
					//  Modo uso
					//		driving [ l0 | l1 | l2 | l3 ]
					//
					//  Ejemplo: Activar nivel de autonomia 3
					//		driving l3
					//      * NOTA: en funciÃ³n del tipo de vÃ­a y su estado
					//          se activarÃ¡ el servicio adecuado para el
					//          nivel indicado
					//
					//
					"driving",
					
				//
				// SIMULADOR
				// 
					
					// next Ã³ n : da un paso de simulaciÃ³n manual
					//
					//  Modo uso
					//		next
					//		n
					//
					"next",
					"n",
					
					"working"
		});
		
		this.commandProvReg = context.registerService(MyCommandProvider.class.getName(),
				new MyCommandProvider(context), props);

		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if ( this.commandProvReg != null )
			this.commandProvReg.unregister();
		
		Activator.context = null;
	}


}
