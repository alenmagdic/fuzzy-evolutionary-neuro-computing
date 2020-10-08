package hr.fer.zemris.fuzzy.sets;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;

public class MutableFuzzySet implements IFuzzySet {

	private double[] memberships;
	private IDomain domain;

	public MutableFuzzySet(IDomain domain) {
		memberships = new double[domain.getCardinality()];
		this.domain = domain;
	}

	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public double getValueAt(DomainElement el) {
		return memberships[domain.indexOfElement(el)];
	}

	public MutableFuzzySet set(DomainElement el, double val) {
		memberships[domain.indexOfElement(el)] = val;
		return this;
	}
}
