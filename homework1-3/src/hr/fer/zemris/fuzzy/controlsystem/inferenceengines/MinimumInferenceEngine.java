package hr.fer.zemris.fuzzy.controlsystem.inferenceengines;

import hr.fer.zemris.fuzzy.controlsystem.settings.OperationsSettings;
import hr.fer.zemris.fuzzy.operations.Operations;

public class MinimumInferenceEngine extends SimpleInferenceEngine {
	private OperationsSettings operationsSettings;
	
	public MinimumInferenceEngine() {
		operationsSettings = new OperationsSettings()
				.setAndOperation(Operations.zadehAnd())
				.setNotOperation(Operations.zadehNot())
				.setOrOperation(Operations.zadehOr())
				.setImplicationOperation(Operations.zadehAnd()); //MAMDANI IMPLIKACIJA
	}

	@Override
	public OperationsSettings getOperationsSettings() {
		return operationsSettings;
	}

}
