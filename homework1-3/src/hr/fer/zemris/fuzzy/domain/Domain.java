package hr.fer.zemris.fuzzy.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Domain implements IDomain {

	@Override
	public int indexOfElement(DomainElement element) {
		Iterator<DomainElement> it = iterator();
		for (int i = 0; it.hasNext(); i++) {
			if (it.next().equals(element)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public DomainElement elementForIndex(int index) {
		Iterator<DomainElement> it = iterator();
		for (int i = 0; it.hasNext(); i++) {
			DomainElement el = it.next();
			if (index == i) {
				return el;
			}
		}
		return null;
	}

	public static IDomain intRange(int first, int last) {
		return new SimpleDomain(first, last);
	}
	
	public static IDomain intRange(int first, int last, int step) {
		return new SimpleDomain(first, last,step);
	}

	public static IDomain combine(IDomain domain1, IDomain domain2) {
		List<SingleComponentDomain> singleCompDomains = new ArrayList<>();
		addDomainComponentsToList(domain1, singleCompDomains);
		addDomainComponentsToList(domain2, singleCompDomains);

		return new CompositeDomain(singleCompDomains.toArray(new SingleComponentDomain[0]));
	}

	private static void addDomainComponentsToList(IDomain domain1, List<SingleComponentDomain> singleCompDomains) {
		for (int i = 0, n = domain1.getNumberOfComponents(); i < n; i++) {
			singleCompDomains.add((SingleComponentDomain) domain1.getComponent(i));
		}

	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Domain)) {
			return false;
		}
		
		Domain otherDomain = (Domain) obj;
		if(otherDomain.getCardinality() != getCardinality()) {
			return false;
		}
		
		for(int i = 0; i < otherDomain.getCardinality(); i++) {
			if(!otherDomain.elementForIndex(i).equals(elementForIndex(i))) {
				return false;
			}
		}
		return true;
	}

}
