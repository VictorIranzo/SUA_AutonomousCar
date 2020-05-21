package sua.autonomouscar.simulation;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class TimedSimulatorStepsManager implements ISimulatorStepsManager {

   protected ISimulator simulator = null;
   protected long millis = 1000;
   protected BundleContext context = null;
   protected ServiceRegistration service_reg = null;

   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
   private ScheduledFuture< ? > timer = null;

   public TimedSimulatorStepsManager(BundleContext context, ISimulator simulator) {
	   this.context = context;
      this.simulator = simulator;
   }

   public ISimulatorStepsManager setSimulationTimeSteps(long millis) {
      this.millis = millis;
      return this;
   }

   @Override
   public ISimulatorStepsManager start() {
	   if ( this.service_reg == null )
		   this.service_reg = this.context.registerService(ISimulatorStepsManager.class, this, null);
      this.start_timer();
      return this;

   }

   @Override
   public ISimulatorStepsManager stop() {
      this.timer.cancel(true);
      return this;
   }


   protected void start_timer() { // En aquesta fase, el timer espera fins sincronitzar-se amb les hores en
                                  // punt, aleshores passa a la fase de crucer

      if (this.timer != null) {
         this.timer.cancel(true);
      }

      this.timer = this.scheduler.scheduleAtFixedRate(new Ticker(this.simulator, this.millis), this.millis, this.millis,
            TimeUnit.MILLISECONDS);

   }

   class Ticker extends TimerTask {

      ISimulator simulator = null;
      long time_lapsed_ms = 0;

      public Ticker(ISimulator simulator, long time_lapsed_ms) {
         this.simulator = simulator;
         this.time_lapsed_ms = time_lapsed_ms;
      }

      @Override
      public void run() {
         this.simulator.next(this.time_lapsed_ms);
      }
   }

}
