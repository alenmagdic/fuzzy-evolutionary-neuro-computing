package hr.fer.zemris.fuzzy.controlsystem.systems.shipsystem;

import java.io.IOException;
import java.util.Scanner;

import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.COADefuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.defuzzifiers.Defuzzifier;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.IInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inferenceengines.ProductInferenceEngine;
import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput;
import hr.fer.zemris.fuzzy.controlsystem.systems.FuzzySystem;

public class ShipControlSystemMain {
	
	public static void main(String[] args) throws IOException {
		   			
		Defuzzifier defuzzifier = new COADefuzzifier();
		IInferenceEngine inferenceEngine = new ProductInferenceEngine();
				
		FuzzySystem fsAcceleration = new AccelerationFuzzySystem(defuzzifier,inferenceEngine);
		FuzzySystem fsRudder = new RudderFuzzySystem(defuzzifier,inferenceEngine);

		int L = 0, D = 0, LK = 0, DK = 0, V = 0, S = 0, acceleration, rudder;
		String line = null;
		
		Scanner input = new Scanner(System.in);
		
		while (true) {
			if ((line = input.nextLine()) != null) {
				if (line.charAt(0) == 'K') {
					input.close();
					break;
				}
				
				Scanner s = new Scanner(line);
				L = s.nextInt();
				D = s.nextInt();
				LK = s.nextInt();
				DK = s.nextInt();
				V = s.nextInt();
				S = s.nextInt();
				s.close();
			}   
			
			SystemInput systemInput = new SystemInput()
					.setInputValue(ShipVariables.L, L)
					.setInputValue(ShipVariables.D, D)
					.setInputValue(ShipVariables.LK, LK)
					.setInputValue(ShipVariables.DK, DK)
					.setInputValue(ShipVariables.V, V)
					.setInputValue(ShipVariables.S, S);
			
			acceleration = fsAcceleration.calculateOutput(systemInput);
			rudder = fsRudder.calculateOutput(systemInput); 

			System.out.println(acceleration + " " + rudder);
			System.out.flush();
		}
		
	}

}
