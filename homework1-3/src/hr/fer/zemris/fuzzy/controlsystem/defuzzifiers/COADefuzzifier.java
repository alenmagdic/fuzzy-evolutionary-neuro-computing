package hr.fer.zemris.fuzzy.controlsystem.defuzzifiers;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class COADefuzzifier implements Defuzzifier {

	@Override
	public int defuzzifySet(IFuzzySet set) {
		if(set.getDomain().getNumberOfComponents() != 1) {
			throw new RuntimeException("Nije moguca defuzifikacija skupa definiranog nad višekomponentnom domenom.");
		}
		
		double membershipsSum = 0;
		double weightSum = 0;
		for(DomainElement el: set.getDomain()) {
			double membershipVal = set.getValueAt(el);
			membershipsSum += membershipVal;
			weightSum += membershipVal * el.getComponentValue(0);
		}
		
		return (int)(weightSum/membershipsSum);
	}

}
