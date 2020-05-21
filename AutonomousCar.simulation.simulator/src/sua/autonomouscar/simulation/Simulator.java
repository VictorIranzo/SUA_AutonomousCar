package sua.autonomouscar.simulation;

import java.util.Collection;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.simulation.interfaces.ISimulationElement;

public class Simulator extends Thing implements ISimulator {

	public static final String STEP = "step";
	protected ISimulatorStepsManager stepsManager = null;
	protected boolean verbose = true;

   public Simulator(BundleContext context, String id) {
	   super(context, id);
	   this.setStepCounter(0);
	   this.addImplementedInterface(ISimulator.class.getName());
   }

   @Override
	public ISimulator setStepsManager(ISimulatorStepsManager manager) {
	   this.stepsManager = manager;
	   return this;
	}
   
   @Override
	public ISimulatorStepsManager getStepsManager() {
		return this.stepsManager;
	}
   
   @Override
   public Integer getStepNumber() {
      return (Integer) this.getProperty(Simulator.STEP);
   }

   protected Integer incrementStepCounter() {
      Integer s = this.getStepNumber();
      this.setStepCounter(++s);
      return s;
   }
   
   private void setStepCounter(int step) {
	   this.setProperty(Simulator.STEP, step);
   }

   @Override
   public ISimulator start() {
      if (this.stepsManager != null) {
         this.stepsManager.start();
      }
      return this;
   }

   @Override
   public ISimulator stop() {
      if (this.stepsManager != null) {
         this.stepsManager.stop();
      }
      return this;
   }
   
   @Override
	public ISimulator setVerboseMode(boolean value) {
		this.verbose = value;
		return this;
	}

   @Override
   public ISimulator next(long time_lapse_millis) {

      this.takeSimulationStep(this.incrementStepCounter(), time_lapse_millis);
      return this;
   }

   
   public void takeSimulationStep(Integer step, long time_lapse_millis) {
	   
	   if ( this.verbose )
		   System.out.println("\n>>> >>> >>> >>> >>> >>> >>> >>>\n>>> STEP " + step + "\n>>> >>> >>> >>> >>> >>> >>> >>>");

	   Collection<ISimulationElement> simulationElements = OSGiUtils.getServices(this.getBundleContext(), ISimulationElement.class, null);
	   
	   if ( simulationElements != null && simulationElements.size() > 0 )
		   for (ISimulationElement e : simulationElements) {
			   e.onSimulationStep(step, time_lapse_millis);
		   }
	   
   }
   
   

   
}
