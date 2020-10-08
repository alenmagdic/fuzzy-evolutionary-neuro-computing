package hr.fer.zemris.fuzzy.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CustomValuesDomain extends SingleComponentDomain {
	private List<DomainElement> elements;

	public CustomValuesDomain(Collection<Integer> values) {
		this.elements = new TreeSet<>(values).stream().map(value -> DomainElement.of(value))
				.collect(Collectors.toList());
	}
	
	public CustomValuesDomain(Integer... values) {
		this(Arrays.asList(values));
	}

	@Override
	public int getCardinality() {
		return elements.size();
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return elements.iterator();
	}

}
