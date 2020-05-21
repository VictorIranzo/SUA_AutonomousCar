package sua.autonomouscar.simulation;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ManualSimulatorStepsManager implements IManualSimulatorStepsManager {

   protected ISimulator simulator = null;
   protected int steps = 0;
   protected BundleContext context = null;
   protected ServiceRegistration service_reg = null;

   public ManualSimulatorStepsManager(BundleContext context, ISimulator simulator) {
	   this.context = context;
       this.simulator = simulator;
   }

   @Override
   public ISimulatorStepsManager start() {
	   if ( this.service_reg == null )
		   this.service_reg = this.context.registerService(IManualSimulatorStepsManager.class, this, null);
      return this;

   }

   @Override
   public ISimulatorStepsManager stop() {
	  if ( this.service_reg != null )
		  this.service_reg.unregister();
      return this;
   }

   
   @Override
	public void next() {
	   this.simulator.next(this.steps);
	}



}
