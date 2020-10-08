package hr.fer.zemris.fuzzy.controlsystem.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RuleBase implements Iterable<IfThenRule> {
	private List<IfThenRule> rules;
	
	public RuleBase() {
		rules = new ArrayList<IfThenRule>();
	}
	
	public RuleBase addRule(IfThenRule rule) {
		rules.add(rule);
		return this;
	}
	
	public int getNumberOfRules() {
		return rules.size();
	}
	
	public IfThenRule getRule(int index) {
		return rules.get(index);
	}
	
	@Override
	public Iterator<IfThenRule> iterator() {
		return rules.iterator();
	}
}
