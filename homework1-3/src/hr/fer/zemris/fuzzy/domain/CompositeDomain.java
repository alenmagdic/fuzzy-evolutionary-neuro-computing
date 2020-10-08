package hr.fer.zemris.fuzzy.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeDomain extends Domain {
	private SingleComponentDomain[] singleComponentDomains;

	public CompositeDomain(SingleComponentDomain[] singleComponentDomains) {
		super();
		this.singleComponentDomains = singleComponentDomains;
	}

	@Override
	public int getCardinality() {
		if (singleComponentDomains.length == 0)
			return 0;

		int cardinality = 1;
		for (SingleComponentDomain d : singleComponentDomains) {
			cardinality *= d.getCardinality();
		}
		return cardinality;
	}

	@Override
	public IDomain getComponent(int componentIndex) {
		return singleComponentDomains[componentIndex];
	}

	@Override
	public int getNumberOfComponents() {
		return singleComponentDomains.length;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new CompositeDomainIterator();
	}

	private class CompositeDomainIterator implements Iterator<DomainElement> {
		private Iterator<DomainElement> iterator;

		public CompositeDomainIterator() {
			List<DomainElement> elements = getElementsList(0);
			iterator = elements.iterator();
		}

		private List<DomainElement> getElementsList(int firstSimpleDomainIndex) {
			List<DomainElement> elems = new ArrayList<DomainElement>();
			Iterator<DomainElement> firstComponentIt = singleComponentDomains[firstSimpleDomainIndex].iterator();
			while (firstComponentIt.hasNext()) {
				DomainElement firstCompElem = firstComponentIt.next();
				if (firstSimpleDomainIndex + 1 == singleComponentDomains.length) {
					elems.add(firstCompElem);
				} else {
					List<DomainElement> sublist = getElementsList(firstSimpleDomainIndex + 1);
					for (DomainElement el : sublist) {
						elems.add(DomainElement.mergeElements(firstCompElem, el));
					}
				}
			}

			return elems;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public DomainElement next() {
			return iterator.next();
		}

	}

}
