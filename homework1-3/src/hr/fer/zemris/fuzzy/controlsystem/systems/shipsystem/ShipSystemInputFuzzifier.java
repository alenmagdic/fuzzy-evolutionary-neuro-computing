package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem;

import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.DISTANCE_DOMAIN_STEP_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.MAX_CHECK_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.MAX_DISTANCE_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.MAX_SPEED_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.MIN_CHECK_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.MIN_DISTANCE_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.MIN_SPEED_VALUE;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.SPEED_DOMAIN_STEP_VALUE;

import hr.fer.zemris.fuzzy.controlsystem.fuzzifier.IInputFuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.fuzzifier.SingleValueFuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput;
import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;


public class ShipSystemInputFuzzifier implements IInputFuzzifier {
	
	@Override
	public FuzzifiedSystemInput fuzzifyInput(SystemInput input) {
		
		SingleValueFuzzifier distanceFzr = new SingleValueFuzzifier(MIN_DISTANCE_VALUE, MAX_DISTANCE_VALUE, DISTANCE_DOMAIN_STEP_VALUE);
		SingleValueFuzzifier speedFzr = new SingleValueFuzzifier(MIN_SPEED_VALUE, MAX_SPEED_VALUE, SPEED_DOMAIN_STEP_VALUE);
		SingleValueFuzzifier checkFzr = new SingleValueFuzzifier(MIN_CHECK_VALUE, MAX_CHECK_VALUE, 1);
			
		return new FuzzifiedSystemInput()
				.setInputValue(ShipVariables.L, fuzzify(distanceFzr, ShipVariables.L, input))
				.setInputValue(ShipVariables.D, fuzzify(distanceFzr, ShipVariables.D, input))
				.setInputValue(ShipVariables.LK, fuzzify(distanceFzr, ShipVariables.LK, input))
				.setInputValue(ShipVariables.DK, fuzzify(distanceFzr, ShipVariables.DK, input))
				.setInputValue(ShipVariables.V, fuzzify(speedFzr, ShipVariables.V, input))
				.setInputValue(ShipVariables.S, fuzzify(checkFzr, ShipVariables.S, input));
	}
	
	private IFuzzySet fuzzify(SingleValueFuzzifier valueFuzzifier, int variableId, SystemInput input) {
		return valueFuzzifier.getFuzzySetForValue(input.getValueForVariable(variableId));
	}
	
	
	
}
