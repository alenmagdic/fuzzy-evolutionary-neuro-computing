package hr.fer.zemris.fuzzy.operations;

import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;
import hr.fer.zemris.fuzzy.sets.MutableFuzzySet;

public class Operations {
	private static final IUnaryFunction ZADEH_NOT = x -> 1 - x;
	private static final IBinaryFunction ZADEH_OR = (x, y) -> Math.max(x, y);
	private static final IBinaryFunction ZADEH_AND = (x, y) -> Math.min(x, y);

	public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction function) {
		MutableFuzzySet resultSet = new MutableFuzzySet(set.getDomain());
		for (DomainElement el : set.getDomain()) {
			resultSet.set(el, function.valueAt(set.getValueAt(el)));
		}

		return resultSet;
	}

	public static IUnaryFunction zadehNot() {
		return ZADEH_NOT;
	}

	public static IBinaryFunction zadehOr() {
		return ZADEH_OR;
	}

	public static IBinaryFunction zadehAnd() {
		return ZADEH_AND;
	}

	public static IBinaryFunction hamacherTNorm(double parameter) {
		return (a, b) -> a * b / (parameter + (1 - parameter) * (a + b - a * b));
	}

	public static IBinaryFunction hamacherSNorm(double parameter) {
		return (a, b) -> (a + b - (2 - parameter) * a * b) / (1 - (1 - parameter) * a * b);
	}
	
	public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction function) {
		if (!set1.getDomain().equals(set2.getDomain())) {
			throw new IllegalArgumentException(
					"Nije moguÄ‡e provesti binarnu operaciju nad skupovima koji imaju razliÄ?ite domene.");
		}

		MutableFuzzySet resultSet = new MutableFuzzySet(set1.getDomain());
		for (DomainElement el : set1.getDomain()) {
			resultSet.set(el, function.valueAt(set1.getValueAt(el), set2.getValueAt(el)));
		}

		return resultSet;
	}
}
