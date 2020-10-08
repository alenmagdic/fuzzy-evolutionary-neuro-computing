package hr.fer.zemris.fuzzy.sets;

public class StandardFuzzySets {

	public static IIntUnaryFunction lFunction(int a, int b) {
		return x -> {
			if (x < a)
				return 1;
			if (x >= b)
				return 0;
			return (double) (b - x) / (b - a);
		};
	}

	public static IIntUnaryFunction gammaFunction(int a, int b) {
		return x -> {
			if (x < a)
				return 0;
			if (x >= b)
				return 1;
			return (double) (x - a) / (b - a);
		};
	}

	public static IIntUnaryFunction lambdaFunction(int a, int b, int g) {
		return x -> {
			if (x < a || x >= g)
				return 0;
			if (x >= a && x < b)
				return (double) (x - a) / (b - a);
			return (double) (g - x) / (g - b);
		};
	}

}
