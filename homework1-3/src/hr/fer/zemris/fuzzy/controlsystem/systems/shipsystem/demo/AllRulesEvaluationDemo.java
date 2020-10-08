package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.demo;

import java.util.Scanner;

import hr.fer.zemris.fuzzy.Debug;
import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.COADefuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.Defuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.ProductInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.SimpleInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput;
import hr.fer.zemris.fuzzy.controlsystem.systems.FuzzySystem;
import hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.RudderFuzzySystem;
import hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem.ShipVariables;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class AllRulesEvaluationDemo {

	public static void main(String[] args) {
		SimpleInferenceEngine engine = new ProductInferenceEngine();
		Defuzzifier def = new COADefuzzifier();
		FuzzySystem fuzzySystem = new RudderFuzzySystem(def, engine);
				
		Scanner sc = new Scanner(System.in);
		while(true) {
			
			System.out.print("Vrijednost L: ");
			int L = sc.nextInt();
			if(L == -1) {
				sc.close();
				return;
			}
			
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
			
			IFuzzySet outputSet = fuzzySystem.calculateOutputFuzzySet(systemInput);
			Debug.print(outputSet, "Izlazni skup: ");
			System.out.println("Dekodirana vrijednost: "+ def.defuzzifySet(outputSet));
			System.out.println();
		}
	}
}
