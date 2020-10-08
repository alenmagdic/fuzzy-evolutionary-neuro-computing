package hr.fer.zemris.fuzzy.domain;

public interface IDomain extends Iterable<DomainElement> {

	int getCardinality();

	IDomain getComponent(int componentIndex);

	int getNumberOfComponents();

	int indexOfElement(DomainElement element);

	DomainElement elementForIndex(int index);
}
