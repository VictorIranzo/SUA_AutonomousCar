package sua.autonomouscar.simulation;

public interface ISimulator  {

	public ISimulator start();
	public ISimulator stop();

	public Integer getStepNumber();

	public ISimulator next(long time_lapse_millis);
	
	public ISimulator setVerboseMode(boolean value);
	
	public ISimulator setStepsManager(ISimulatorStepsManager manager);
	public ISimulatorStepsManager getStepsManager();
	
}
