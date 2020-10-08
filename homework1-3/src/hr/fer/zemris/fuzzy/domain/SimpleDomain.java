package hr.fer.zemris.fuzzy.domain;

import java.util.Iterator;

public class SimpleDomain extends SingleComponentDomain {
	private int first;
	private int last;
	private int step;

	public SimpleDomain(int first, int last, int step) {
		this.first = first;
		this.last = last;
		this.step = step;
	}
	
	public SimpleDomain(int first, int last) {
		this(first,last,1);
	}

	@Override
	public int getCardinality() {
		return (int) Math.ceil((last-first)/(double)step);
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new SimpleDomainIterator();
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}
	
	public int getStep() {
		return step;
	}

	private class SimpleDomainIterator implements Iterator<DomainElement> {
		int next;

		public SimpleDomainIterator() {
			next = first;
		}

		@Override
		public boolean hasNext() {
			return next < last;
		}

		@Override
		public DomainElement next() {
			int val = next;
			next+=step;
			return new DomainElement(new int[] { val });
		}

	}

}
