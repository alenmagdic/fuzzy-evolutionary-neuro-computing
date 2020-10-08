package hr.fer.zemris.fuzzy.controlsystem.rules;

import hr.fer.zemris.fuzzy.controlsystem.settings.OperationsSettings;
import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.operations.Operations;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class PredicateSet implements IFuzzySet {
	private OperationsSettings operationsSettings;
	private IFuzzySet predicateSet;
	
	public PredicateSet(OperationsSettings operationsSettings) {
		this.operationsSettings = operationsSettings;
	}
	
	public PredicateSet is(IFuzzySet simplePredicateSet) {
		predicateSet = simplePredicateSet;
		return this;
	}
	
	public PredicateSet andIs(IFuzzySet simplePredicateSet) {
		predicateSet = Operations.binaryOperation(predicateSet, simplePredicateSet, operationsSettings.getAndOperation());
		return this;
	}
	
	public PredicateSet orIs(IFuzzySet simplePredicateSet) {
		predicateSet = Operations.binaryOperation(predicateSet, simplePredicateSet, operationsSettings.getOrOperation());
		return this;
	}
	
	public PredicateSet andIsNot(IFuzzySet simplePredicateSet) {
		return andIs(not(simplePredicateSet));
	}
	
	public PredicateSet orIsNot(IFuzzySet simplePredicateSet) {
		return orIs(not(simplePredicateSet));
	}
	
	private IFuzzySet not(IFuzzySet set) {
		return Operations.unaryOperation(set, operationsSettings.getNotOperation());
	}

	@Override
	public IDomain getDomain() {
		return predicateSet.getDomain();
	}

	@Override
	public double getValueAt(DomainElement el) {
		return predicateSet.getValueAt(el);
	}

	public PredicateSet isNot(IFuzzySet simplePredicateSet) {
		return is(not(simplePredicateSet));
	}
}