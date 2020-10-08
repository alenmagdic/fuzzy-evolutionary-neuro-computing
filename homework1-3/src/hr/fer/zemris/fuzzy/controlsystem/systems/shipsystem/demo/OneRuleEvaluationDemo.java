package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.demo;

import java.util.Scanner;

import hr.fer.zemris.fuzzy.Debug;
import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.COADefuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.Defuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.MinimumInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.SimpleInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput;
import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleBase;
import hr.fer.zemris.fuzzy.controlsystem.systems.FuzzySystem;
import hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.RudderFuzzySystem;
import hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipSystemInputFuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipVariables;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class OneRuleEvaluationDemo {

	public static void main(String[] args) {
		SimpleInferenceEngine engine = new MinimumInferenceEngine();
		Defuzzifier def = new COADefuzzifier();
		FuzzySystem fuzzySystem = new RudderFuzzySystem(def, engine);
		RuleBase ruleBase = fuzzySystem.getRuleBase();
				
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.printf("Redni broj pravila iz baze pravila [1,%d]: ",ruleBase.getNumberOfRules());
			int ruleNumber = sc.nextInt();
			if(ruleNumber == 0) {
				sc.close();
				return;
			}
			
			System.out.print("Vrijednost L: ");
			int L = sc.nextInt();
			
			System.out.println("Vrijednost D: ");
			int D = sc.nextInt();
			
			System.out.println("Vrijednost LK: ");
			int LK = sc.nextInt();
			
			System.out.println("Vrijednost DK: ");
			int DK = sc.nextInt();
			
			System.out.println("Vrijednost V: ");
			int V = sc.nextInt();
			
			System.out.println("Vrijednost S: ");
			int S = sc.nextInt();
			
			SystemInput systemInput = new SystemInput()
					.setInputValue(ShipVariables.L, L)
					.setInputValue(ShipVariables.D, D)
					.setInputValue(ShipVariables.LK, LK)
					.setInputValue(ShipVariables.DK, DK)
					.setInputValue(ShipVariables.V, V)
					.setInputValue(ShipVariables.S, S);
			
			FuzzifiedSystemInput sysInFuzz = new ShipSystemInputFuzzifier().fuzzifyInput(systemInput);
			IFuzzySet outputSet = engine.calculateOutputForRule(sysInFuzz, ruleBase.getRule(ruleNumber-1));
			Debug.print(outputSet, "Izlazni skup: ");
			System.out.println("Dekodirana vrijednost: "+ def.defuzzifySet(outputSet));
			System.out.println();
		}
	}
}
