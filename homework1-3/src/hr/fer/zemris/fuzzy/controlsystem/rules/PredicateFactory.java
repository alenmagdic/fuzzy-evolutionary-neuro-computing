package hr.fer.zemris.fuzzy.controlsystem.rules;

import hr.fer.zemris.fuzzy.controlsystem.settings.OperationsSettings;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class PredicateFactory {
	private OperationsSettings settings;
	
	public PredicateFactory(OperationsSettings settings) {
		this.settings = settings;
	}
	
	public PredicateSet is(IFuzzySet simplePredicateSet) {
		return new PredicateSet(settings).is(simplePredicateSet);
	}
	
	public PredicateSet isNot(IFuzzySet simplePredicateSet) {
		return new PredicateSet(settings).isNot(simplePredicateSet);
	}
}
