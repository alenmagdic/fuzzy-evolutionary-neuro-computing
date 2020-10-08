package hr.fer.zemris.fuzzy.controlsystem.fuzzifier;

public class DomainValueMatcher {

	public static int getClosestDomainValueTo(int value,int minDomainValue, int maxDomainValue,int domainStepValue) {
		if(value > maxDomainValue) {
			value = maxDomainValue;
		}

		int closestLowerValue = value - value%domainStepValue; //rezanje ostatka u odnosu na korak (npr. 57 se postavlja na 40 ako je korak 40)
		int closestHigherValue = closestLowerValue + domainStepValue;
		int distanceToClosestLowerValue = Math.abs(value-closestLowerValue);
		int distanceToClosestHigherValue = Math.abs(value-closestHigherValue);
		if(closestHigherValue <= maxDomainValue && distanceToClosestHigherValue < distanceToClosestLowerValue) {
			return closestHigherValue;
		} else {
			return closestLowerValue;
		}
	}
}
