package hr.fer.zemris.fuzzy.controlsystem.fuzzifier;

import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput;
import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput;

public interface IInputFuzzifier {

	public FuzzifiedSystemInput fuzzifyInput(SystemInput input);
}
