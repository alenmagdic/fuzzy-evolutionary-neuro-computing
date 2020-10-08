package hr.fer.zemris.fuzzy.controlsystem.inferenceengines;

import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleBase;
import hr.fer.zemris.fuzzy.controlsystem.settings.OperationsSettings;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public interface IInferenceEngine {

	public OperationsSettings getOperationsSettings();
	
	public IFuzzySet calculateOutput(FuzzifiedSystemInput input,RuleBase ruleBase); 
}
