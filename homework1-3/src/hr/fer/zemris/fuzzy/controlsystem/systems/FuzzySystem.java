package hr.fer.zemris.fuzzy.controlsystem.systems;

import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.Defuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.fuzzifier.IInputFuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.IInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput;
import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleBase;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public abstract class FuzzySystem {
	private IInputFuzzifier fuzzifier;
	private Defuzzifier defuzzifier;
	private IInferenceEngine inferenceEngine;
	
	public FuzzySystem(IInputFuzzifier fuzzifier,IInferenceEngine inferenceEngine,Defuzzifier defuzzifier) {
		this.fuzzifier = fuzzifier;
		this.defuzzifier = defuzzifier;
		this.inferenceEngine = inferenceEngine;
	}

	public int calculateOutput(SystemInput input) {
		return defuzzifier.defuzzifySet(calculateOutputFuzzySet(input));
	}
	
	public IFuzzySet calculateOutputFuzzySet(SystemInput input) {
		FuzzifiedSystemInput fuzzifiedInput = fuzzifier.fuzzifyInput(input);
		IFuzzySet outputFuzzySet = inferenceEngine.calculateOutput(fuzzifiedInput,getRuleBase());
		return outputFuzzySet;
	}
	
	public IInferenceEngine getInferenceEngine() {
		return inferenceEngine;
	}

	public abstract RuleBase getRuleBase();
}
