package hr.fer.zemris.fuzzy.domain;

import java.util.Arrays;

public class DomainElement {
	private int[] values;

	public DomainElement(int[] values) {
		this.values = values;
	}

	public int getNumberOfComponents() {
		return values.length;
	}

	public int getComponentValue(int componentIndex) {
		return values[componentIndex];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(values);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomainElement other = (DomainElement) obj;
		if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (values.length == 1) {
			return Integer.toString(values[0]);
		}

		return Arrays.toString(values).replace('[', '(').replace(']', ')');
	}

	public static DomainElement of(int... values) {
		return new DomainElement(values);
	}

	public static DomainElement mergeElements(DomainElement el1, DomainElement el2) {
		int[] mergedValues = Arrays.copyOf(el1.values, el1.values.length + el2.values.length);
		for (int i = 0; i < el2.values.length; i++) {
			mergedValues[el1.values.length + i] = el2.values[i];
		}

		return new DomainElement(mergedValues);
	}

}
