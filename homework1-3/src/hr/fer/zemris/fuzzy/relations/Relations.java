package hr.fer.zemris.fuzzy.relations;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.fuzzy.domain.CustomValuesDomain;
import hr.fer.zemris.fuzzy.domain.Domain;
import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;
import hr.fer.zemris.fuzzy.sets.MutableFuzzySet;

public class Relations {

	public static boolean isUtimesURelation(IFuzzySet relation) {
		IDomain domain = relation.getDomain();
		if (domain.getNumberOfComponents() != 2) {
			return false;
		}

		Set<Integer> firstCompValues = getAllValuesOfDomainComponent(domain, 0);
		Set<Integer> secondCompValues = getAllValuesOfDomainComponent(domain, 1);

		return firstCompValues.equals(secondCompValues);
	}

	private static Set<Integer> getAllValuesOfDomainComponent(IDomain domain, int componentIndex) {
		Set<Integer> componentValues = new HashSet<>();
		for (DomainElement el : domain) {
			componentValues.add(el.getComponentValue(componentIndex));
		}
		return componentValues;
	}

	public static boolean isSymmetric(IFuzzySet relation) {
		IDomain domain = relation.getDomain();
		if (!isUtimesURelation(relation)) {
			throw new IllegalArgumentException("Zadana relacija koja nije definirana nad UxU domenom");
		}

		for (DomainElement el : domain) {
			int comp1 = el.getComponentValue(0);
			int comp2 = el.getComponentValue(1);
			DomainElement symmetricElem = DomainElement.of(comp2, comp1);

			if (relation.getValueAt(el) != relation.getValueAt(symmetricElem)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isReflexive(IFuzzySet relation) {
		IDomain domain = relation.getDomain();
		if (!isUtimesURelation(relation)) {
			throw new IllegalArgumentException("Zadana relacija koja nije definirana nad UxU domenom");
		}

		for (DomainElement el : domain) {
			int comp1 = el.getComponentValue(0);
			int comp2 = el.getComponentValue(1);
			if (comp1 != comp2) {
				continue;
			}

			if (relation.getValueAt(el) != 1.0) {
				return false;
			}
		}

		return true;
	}

	public static boolean isMaxMinTransitive(IFuzzySet relation) {
		if (!isUtimesURelation(relation)) {
			throw new IllegalArgumentException("Zadana relacija koja nije definirana nad UxU domenom");
		}

		Set<Integer> secondCompValues = getAllValuesOfDomainComponent(relation.getDomain(), 1);
		for (DomainElement el : relation.getDomain()) {
			// oznake x,y,z kao u definicij tranzitivnosti
			int x = el.getComponentValue(0);
			int y = el.getComponentValue(1);
			for (int z : secondCompValues) {
				double xyValue = relation.getValueAt(el);
				double xzValue = relation.getValueAt(DomainElement.of(x, z));
				double yzValue = relation.getValueAt(DomainElement.of(y, z));

				if (xzValue < Math.min(xyValue, yzValue)) {
					return false;
				}
			}
		}

		return true;
	}

	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet r1, IFuzzySet r2) {
		Set<Integer> r1FirstCompValues = getAllValuesOfDomainComponent(r1.getDomain(), 0);
		Set<Integer> r1SecondCompValues = getAllValuesOfDomainComponent(r1.getDomain(), 1);
		Set<Integer> r2FirstCompValues = getAllValuesOfDomainComponent(r2.getDomain(), 0);
		Set<Integer> r2SecondCompValues = getAllValuesOfDomainComponent(r2.getDomain(), 1);
		if (!r1SecondCompValues.equals(r2FirstCompValues)) {
			throw new IllegalArgumentException("Nekompatibilne domene relacija");
		}

		IDomain resultDomain = Domain.combine(new CustomValuesDomain(r1FirstCompValues),
				new CustomValuesDomain(r2SecondCompValues));

		MutableFuzzySet resultSet = new MutableFuzzySet(resultDomain);
		for (DomainElement el : resultDomain) {
			// oznake x,y,z kao u definiciji kompozicije
			int x = el.getComponentValue(0);
			int z = el.getComponentValue(1);
			double maxConnStrength = 0;
			for (int y : r1SecondCompValues) {
				double xyValue = r1.getValueAt(DomainElement.of(x, y));
				double yzValue = r2.getValueAt(DomainElement.of(y, z));

				double connStrength = Math.min(xyValue, yzValue);
				if (connStrength > maxConnStrength) {
					maxConnStrength = connStrength;
				}
			}

			resultSet.set(el, maxConnStrength);
		}

		return resultSet;
	}

	public static boolean isFuzzyEquivalence(IFuzzySet r) {
		return isSymmetric(r) && isReflexive(r) && isMaxMinTransitive(r);
	}

}
