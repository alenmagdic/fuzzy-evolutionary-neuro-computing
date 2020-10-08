package hr.fer.zemris.fuzzy;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class Debug {

	public static void print(IDomain domain, String headingText) {
		if (headingText != null) {
			System.out.println(headingText);
		}
		for (DomainElement e : domain) {
			System.out.println("Element domene: " + e);
		}
		System.out.println("Kardinalitet domene je: " + domain.getCardinality());
		System.out.println();
	}

	public static void print(IFuzzySet set1, String headingText) {
		System.out.println(headingText);
		for (DomainElement el : set1.getDomain()) {
			System.out.printf("d(%s)=%f\n", el.toString(), set1.getValueAt(el));
		}

	}

}
