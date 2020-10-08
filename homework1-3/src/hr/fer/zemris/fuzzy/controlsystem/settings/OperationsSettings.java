package hr.fer.zemris.fuzzy.controlsystem.settings;

import hr.fer.zemris.fuzzy.operations.IBinaryFunction;
import hr.fer.zemris.fuzzy.operations.IUnaryFunction;
import hr.fer.zemris.fuzzy.operations.Operations;

public class OperationsSettings {
	private static final IBinaryFunction MAMDANI_IMPLICATION = Operations.zadehAnd();
	
	private IBinaryFunction implicationOperation;
	private IBinaryFunction andOperation;
	private IBinaryFunction orOperation;
	private IUnaryFunction notOperation;
	
	public OperationsSettings() {
		implicationOperation = MAMDANI_IMPLICATION;
		andOperation = Operations.zadehAnd();
		orOperation = Operations.zadehOr();
		notOperation = Operations.zadehNot();		
	}


	public IBinaryFunction getImplicationOperation() {
		return implicationOperation;
	}

	public OperationsSettings setImplicationOperation(IBinaryFunction implicationOperation) {
		this.implicationOperation = implicationOperation;
		return this;
	}

	public IBinaryFunction getAndOperation() {
		return andOperation;
	}

	public OperationsSettings setAndOperation(IBinaryFunction andOperation) {
		this.andOperation = andOperation;
		return this;
	}

	public IBinaryFunction getOrOperation() {
		return orOperation;
	}

	public OperationsSettings setOrOperation(IBinaryFunction orOperation) {
		this.orOperation = orOperation;
		return this;
	}

	public IUnaryFunction getNotOperation() {
		return notOperation;
	}

	public OperationsSettings setNotOperation(IUnaryFunction notOperation) {
		this.notOperation = notOperation;
		return this;
	}

}
