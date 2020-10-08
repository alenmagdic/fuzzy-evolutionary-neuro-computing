package hr.fer.zemris.fuzzy.controlsystem.fuzzifier;

import hr.fer.zemris.fuzzy.domain.Domain;
import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;
import hr.fer.zemris.fuzzy.sets.MutableFuzzySet;

public class SingleValueFuzzifier {
	private int minDomainValue;
	private int maxDomainValue;
	private int domainStepValue;
	
	public SingleValueFuzzifier(int minDomainValue, int maxDomainValue, int domainStepValue) {
		this.minDomainValue = minDomainValue;
		this.maxDomainValue = maxDomainValue;
		this.domainStepValue = domainStepValue;
	}
	
	
	public IFuzzySet getFuzzySetForValue(int distance) {
		IDomain domain = Domain.intRange(minDomainValue, maxDomainValue+1, domainStepValue);
		
		int elementValue = DomainValueMatcher.getClosestDomainValueTo(distance, minDomainValue, maxDomainValue, domainStepValue);
				
		return new MutableFuzzySet(domain).set(DomainElement.of(elementValue), 1.0);
		
	}
}
