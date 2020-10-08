package hr.fer.zemris.fuzzy.controlsystem.rules;

import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class RuleFactory {
		
	public IfThenRule ifIs(int variableId,IFuzzySet simplePredicateSet) {
		return new IfThenRule().ifIs(variableId, simplePredicateSet);
	}

}
