package sua.autonomouscar.simulation.interfaces;


public interface ISimulationElement {

   public void onSimulationStep(Integer step, long time_lapse_millis);

}
