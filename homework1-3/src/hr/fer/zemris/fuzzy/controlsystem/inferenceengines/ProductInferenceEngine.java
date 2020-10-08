package hr.fer.zemris.fuzzy.controlsystem.inferenceengines;

import hr.fer.zemris.fuzzy.controlsystem.settings.OperationsSettings;
import hr.fer.zemris.fuzzy.operations.Operations;

public class ProductInferenceEngine extends SimpleInferenceEngine {

	private OperationsSettings operationsSettings;
	
	public ProductInferenceEngine() {
		operationsSettings = new OperationsSettings()
				.setAndOperation((a,b) -> a*b)
				.setNotOperation(Operations.zadehNot())
				.setOrOperation(Operations.zadehOr())
				.setImplicationOperation((a,b) -> a*b);
	}

	@Override
	public OperationsSettings getOperationsSettings() {
		return operationsSettings;
	}

}
