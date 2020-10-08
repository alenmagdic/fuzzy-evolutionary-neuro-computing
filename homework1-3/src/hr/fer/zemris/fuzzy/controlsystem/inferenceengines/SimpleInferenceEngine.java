package hr.fer.zemris.fuzzy.controlsystem.inferenceengines;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput;
import hr.fer.zemris.fuzzy.controlsystem.rules.IfThenRule;
import hr.fer.zemris.fuzzy.controlsystem.rules.RuleBase;
import hr.fer.zemris.fuzzy.domain.DomainElement;
import hr.fer.zemris.fuzzy.operations.Operations;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;
import hr.fer.zemris.fuzzy.sets.MutableFuzzySet;


public abstract class SimpleInferenceEngine implements IInferenceEngine{
			
	@Override
	public IFuzzySet calculateOutput(FuzzifiedSystemInput input, RuleBase ruleBase) {
		List<IFuzzySet> rulesOutputs = new ArrayList<>();
		for(IfThenRule rule : ruleBase) {
			rulesOutputs.add(calculateOutputForRule(input, rule));
		}
		
		return combineIntoSingleOutput(rulesOutputs);
	}
	
	public IFuzzySet combineIntoSingleOutput(List<IFuzzySet> rulesOutputs) {
		IFuzzySet resultSet = null;
		for(IFuzzySet set : rulesOutputs) {
			if(resultSet == null) {
				resultSet = set;
				continue;
			}
			resultSet = Operations.binaryOperation(resultSet, set, getOperationsSettings().getOrOperation());
		}
		return resultSet;
	}
		
	private int getElementFromSingletonSet(IFuzzySet set) {
		for(DomainElement el : set.getDomain()) {
			if(set.getValueAt(el) > 0.98) {
				return el.getComponentValue(0); //set je jednokomponentni
			}
		}
		throw new RuntimeException("Nijedan element skupa nema mjeru pripadnosti vecu od nula");
	}

	public IFuzzySet calculateOutputForRule(FuzzifiedSystemInput input, IfThenRule rule) {
		IFuzzySet consequentSet = rule.getConsequentSet();
		MutableFuzzySet ruleOutput = new MutableFuzzySet(consequentSet.getDomain());
		
		for(DomainElement el : consequentSet.getDomain()) {
			
			List<Double> ruleMembershipsForInputValues = new ArrayList<>();
			
			for(FuzzifiedSystemInput.FuzzyInputValue inputValue : input) {
				IFuzzySet inputValueRuleComponent = rule.getComponentForVariable(inputValue.getVariableId());
				if(inputValueRuleComponent == null) { //pravilo ne uzima u obzir ovu input varijablu
					continue;
				}	
				int inputValueDefuzzified = getElementFromSingletonSet(inputValue.getValueSet()); 

				ruleMembershipsForInputValues.add(
						inputValueRuleComponent.getValueAt(DomainElement.of(inputValueDefuzzified))
						);
			}
			
			double antecedentValue = calculateTNorm(ruleMembershipsForInputValues);
			double consequentValue = consequentSet.getValueAt(el);

			ruleOutput.set(el, getOperationsSettings().getImplicationOperation().valueAt(antecedentValue, consequentValue));
		}
		
		return ruleOutput;
	}
	
	private double calculateTNorm(List<Double> values) {
		Double result = null;
		for(Double val : values) {
			if(result == null) {
				result = val;
				continue;
			}
			result = getOperationsSettings().getAndOperation().valueAt(result, val);
		}
		
		return result;
	}


}
