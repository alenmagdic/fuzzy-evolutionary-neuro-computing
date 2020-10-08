package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem;
import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.*;

import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.Defuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.IInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.rules.PredicateFactory;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleBase;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleFactory;
import hr.fer.zemris.fuzzy.controlsystem.settings.OperationsSettings;
import hr.fer.zemris.fuzzy.controlsystem.systems.FuzzySystem;

public class AccelerationFuzzySystem extends FuzzySystem {
	private RuleBase ruleBase;

	public AccelerationFuzzySystem(Defuzzifier defuzzifier,IInferenceEngine inferenceEngine) {
		super(new ShipSystemInputFuzzifier(), inferenceEngine, defuzzifier);
		generateRuleBase();
	}
	
	private void generateRuleBase() {
		ruleBase = new RuleBase();
		
		OperationsSettings opSettings = getInferenceEngine().getOperationsSettings();
		PredicateFactory pf = new PredicateFactory(opSettings);
		
		RuleFactory rf = new RuleFactory();
				
		ruleBase
			
			.addRule(
					rf.ifIs(ShipVariables.L, IS_LOW_DISTANCE)
					.then(NORMAL_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.D, IS_LOW_DISTANCE)
					.then(NORMAL_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.DK, IS_LOW_DISTANCE)
					.then(NORMAL_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.LK, IS_LOW_DISTANCE)
					.then(NORMAL_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.L, IS_VERY_LOW_DISTANCE)
					.then(MAX_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.D, IS_VERY_LOW_DISTANCE)
					.then(MAX_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.DK, IS_VERY_LOW_DISTANCE)
					.then(MAX_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.LK, IS_VERY_LOW_DISTANCE)
					.then(MAX_ACCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.V, pf.is(IS_LOW_SPEED).orIs(IS_NORMAL_SPEED))
					.then(NORMAL_ACCELERATION)
			)
						
			.addRule(
					rf.ifIs(ShipVariables.V, IS_HIGH_SPEED)
					.then(NORMAL_DECCELERATION)
			)
			
			.addRule(
					rf.ifIs(ShipVariables.LK, IS_LOW_DISTANCE)
					.andIf(ShipVariables.DK, IS_LOW_DISTANCE)
					.andIf(ShipVariables.V, IS_HIGH_SPEED)
					.then(MAX_DECCELERATION)
			)
							

			;
		
	}

	@Override
	public RuleBase getRuleBase() {
		return ruleBase;
	}
	
	

}
