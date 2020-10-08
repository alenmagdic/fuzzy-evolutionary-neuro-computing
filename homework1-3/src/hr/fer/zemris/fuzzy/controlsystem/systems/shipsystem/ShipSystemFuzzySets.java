package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem;

import hr.fer.zemris.fuzzy.controlsystem.fuzzifier.DomainValueMatcher;
import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.domain.SimpleDomain;
import hr.fer.zemris.fuzzy.sets.CalculatedFuzzySet;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;
import hr.fer.zemris.fuzzy.sets.IIntUnaryFunction;
import hr.fer.zemris.fuzzy.sets.MutableFuzzySet;
import hr.fer.zemris.fuzzy.sets.StandardFuzzySets;

public class ShipSystemFuzzySets {
	public static final int MIN_DISTANCE_VALUE = 0;
	public static final int MAX_DISTANCE_VALUE = 1300;
	public static final int DISTANCE_DOMAIN_STEP_VALUE = 10;
	
	public static final int MIN_SPEED_VALUE = 0;
	public static final int MAX_SPEED_VALUE = 200;
	public static final int SPEED_DOMAIN_STEP_VALUE = 10;
	
	public static final int MIN_CHECK_VALUE = 0;
	public static final int MAX_CHECK_VALUE = 1;
	
	public static final int MIN_ACCELERATION_VALUE = -15;
	public static final int MAX_ACCELERATION_VALUE = 15;
	public static final int ACCELERATION_DOMAIN_STEP_VALUE = 1;
	
	public static final int MIN_RUDDER_VALUE = -90;
	public static final int MAX_RUDDER_VALUE = 90;
	public static final int RUDDER_DOMAIN_STEP_VALUE = 5;

	
	public static SimpleDomain getDistanceDomain() {
		return new SimpleDomain(MIN_DISTANCE_VALUE, MAX_DISTANCE_VALUE+1, DISTANCE_DOMAIN_STEP_VALUE);
	}
	
	public static SimpleDomain getSpeedDomain() {
		return new SimpleDomain(MIN_SPEED_VALUE, MAX_SPEED_VALUE+1, SPEED_DOMAIN_STEP_VALUE);
	}
	
	public static SimpleDomain getCheckDomain() {
		return new SimpleDomain(MIN_CHECK_VALUE, MAX_CHECK_VALUE+1);
	}
	
	public static SimpleDomain getAccelerationDomain() {
		return new SimpleDomain(MIN_ACCELERATION_VALUE, MAX_ACCELERATION_VALUE+1, ACCELERATION_DOMAIN_STEP_VALUE);
	}
	
	public static SimpleDomain getRudderDomain() {
		return new SimpleDomain(MIN_RUDDER_VALUE, MAX_RUDDER_VALUE+1, RUDDER_DOMAIN_STEP_VALUE);
	}
	
	public static final IFuzzySet IS_HIGH_DISTANCE = gammaSet(getDistanceDomain(), 200, 500);
	
	public static final IFuzzySet IS_VERY_HIGH_DISTANCE = gammaSet(getDistanceDomain(), 450, 750);
	
	public static final IFuzzySet IS_NORMAL_DISTANCE = lambdaSet(getDistanceDomain(), 70, 130,200);
	
	public static final IFuzzySet IS_LOW_DISTANCE =
		lSet(getDistanceDomain(), 30, 60);

	
	public static final IFuzzySet IS_VERY_LOW_DISTANCE =
		lSet(getDistanceDomain(), 0, 40);
	
	
	public static final IFuzzySet IS_LOW_SPEED =
		lSet(getSpeedDomain(), 10, 40);
	
	public static final IFuzzySet IS_VERY_LOW_SPEED =
			lSet(getSpeedDomain(), 0, 20);
	
	
	public static final IFuzzySet IS_HIGH_SPEED =
		 gammaSet(getSpeedDomain(), 45, 80);
	
	public static final IFuzzySet IS_VERY_HIGH_SPEED =
			 gammaSet(getSpeedDomain(), 120, 200);
	
	
	public static final IFuzzySet IS_NORMAL_SPEED =
		 lambdaSet(getSpeedDomain(), 30, 60, 90);
	

	public static final IFuzzySet IS_CHECK_OK =
		 new MutableFuzzySet(getCheckDomain()).set(DomainElement.of(1), 1.0);
	
	
	public static final IFuzzySet IS_CHECK_NOT_OK =
		new MutableFuzzySet(getCheckDomain()).set(DomainElement.of(0), 1.0);
	
	
	public static final IFuzzySet MAX_ACCELERATION =
		 gammaSet(getAccelerationDomain(), 12,15);
		
	
	public static final IFuzzySet NORMAL_ACCELERATION =
		 lambdaSet(getAccelerationDomain(), 7,9,11);
	
	
	public static final IFuzzySet ZERO_ACCELERATION =
		lambdaSet(getAccelerationDomain(), -3,0,3);
	
	
	public static final IFuzzySet NORMAL_DECCELERATION =
		 lambdaSet(getAccelerationDomain(), -11,-9,-7);
	
	
	public static final IFuzzySet MAX_DECCELERATION =
		 lSet(getAccelerationDomain(), -12,-15);
	
	
	public static final IFuzzySet TURN_HARD_LEFT =
		 gammaSet(getRudderDomain(), 75,90);
	
	public static final IFuzzySet TURN_GENTLY_LEFT =
			 lambdaSet(getRudderDomain(), 15,25,40);
		
	
	public static final IFuzzySet TURN_LEFT =
		 lambdaSet(getRudderDomain(), 30,45,60);
		
	
	public static final IFuzzySet NEUTRAL_RUDDER_POSITION =
		 lambdaSet(getRudderDomain(), -10,0,10);
	
	
	public static final IFuzzySet TURN_RIGHT =
		 lambdaSet(getRudderDomain(), -60,-45,-30);
	
	
	public static final IFuzzySet TURN_HARD_RIGHT =
		 lSet(getRudderDomain(), -90,-75);
	
	public static final IFuzzySet TURN_GENTLY_RIGHT =
			 lambdaSet(getRudderDomain(), -40,-25,-10);
	
	
	private static IFuzzySet lambdaSet(SimpleDomain domain, int minValue, int middleValue, int maxValue) {
		IIntUnaryFunction fuzzySetCalculator = StandardFuzzySets.lambdaFunction(
				indexOfClosestDomainElementTo(minValue, domain),
				indexOfClosestDomainElementTo(middleValue, domain),
				indexOfClosestDomainElementTo(maxValue, domain));
		
		return new CalculatedFuzzySet(domain, fuzzySetCalculator);
	}
	
	private static IFuzzySet lSet(SimpleDomain domain, int minValue, int maxValue) {
		IIntUnaryFunction fuzzySetCalculator = StandardFuzzySets.lFunction(
				indexOfClosestDomainElementTo(minValue, domain),
				indexOfClosestDomainElementTo(maxValue, domain));
		
		return new CalculatedFuzzySet(domain, fuzzySetCalculator);
	}
	
	private static IFuzzySet gammaSet(SimpleDomain domain, int minValue, int maxValue) {
		IIntUnaryFunction fuzzySetCalculator = StandardFuzzySets.gammaFunction(
				indexOfClosestDomainElementTo(minValue, domain),
				indexOfClosestDomainElementTo(maxValue, domain));
		
		return new CalculatedFuzzySet(domain, fuzzySetCalculator);
	}
	
	
	private static int indexOfClosestDomainElementTo(double value,SimpleDomain domain) {
		int closestDomainValue = DomainValueMatcher.getClosestDomainValueTo((int)value, domain.getFirst(), domain.getLast()-1, domain.getStep());
		return domain.indexOfElement(DomainElement.of(closestDomainValue));
	}
	
}
