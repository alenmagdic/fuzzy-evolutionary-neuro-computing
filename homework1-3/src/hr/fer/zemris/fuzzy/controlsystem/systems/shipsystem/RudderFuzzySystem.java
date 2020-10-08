package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem;

import static hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemFuzzySets.*;

import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.Defuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.IInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleBase;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleFactory;
import hr.fer.zemris.fuzzy.controlsystem.systems.FuzzySystem;

public class RudderFuzzySystem extends FuzzySystem {
	private RuleBase ruleBase;

	public RudderFuzzySystem(Defuzzifier defuzzifier,IInferenceEngine inferenceEngine) {
		super(new ShipSystemInputFuzzifier(), inferenceEngine, defuzzifier); 
		generateRuleBase();
	}
	
	private void generateRuleBase() {
		ruleBase = new RuleBase();
		
		//OperationsSettings opSettings = getInferenceEngine().getOperationsSettings();
		//PredicateFactory pf = new PredicateFactory(opSettings);
		
		RuleFactory rf = new RuleFactory();
			
		ruleBase
			.addRule(
					rf.ifIs(ShipVariables.LK, IS_LOW_DISTANCE)
					.then(TURN_HARD_RIGHT)
					)			
			
			.addRule(
					rf.ifIs(ShipVariables.DK, IS_LOW_DISTANCE)
					.then(TURN_HARD_LEFT)
					)
			
			.addRule(
					rf.ifIs(ShipVariables.LK,IS_NORMAL_DISTANCE)
					.andIf(ShipVariables.DK, IS_HIGH_DISTANCE)
					.then(TURN_GENTLY_RIGHT)
					)
			
			.addRule(
					rf.ifIs(ShipVariables.DK,IS_NORMAL_DISTANCE)
					.andIf(ShipVariables.LK, IS_HIGH_DISTANCE)
					.then(TURN_GENTLY_LEFT)
					)
			
			

			;
					
	}


	@Override
	public RuleBase getRuleBase() {
		return ruleBase;
	}
	
}
