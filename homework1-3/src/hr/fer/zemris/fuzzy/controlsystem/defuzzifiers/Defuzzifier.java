package hr.fer.zemris.fuzzy.controlsystem.defuzzifiers;

import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public interface Defuzzifier {

	public int defuzzifySet(IFuzzySet set);
}
