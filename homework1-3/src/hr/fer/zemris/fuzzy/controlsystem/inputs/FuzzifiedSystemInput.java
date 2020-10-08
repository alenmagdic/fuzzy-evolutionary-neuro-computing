package hr.fer.zemris.fuzzy.controlsystem.inputs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hr.fer.zemris.fuzzy.controlsystem.inputs.FuzzifiedSystemInput.FuzzyInputValue;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class FuzzifiedSystemInput implements Iterable<FuzzyInputValue> {
	public Map<Integer,IFuzzySet> inputMap;
	
	public class FuzzyInputValue {
		int variableId;
		IFuzzySet valueSet;
		public FuzzyInputValue(int variableId, IFuzzySet valueSet) {
			super();
			this.variableId = variableId;
			this.valueSet = valueSet;
		}
		
		public int getVariableId() {
			return variableId;
		}
		
		public IFuzzySet getValueSet() {
			return valueSet;
		}
		
	}
	
	public FuzzifiedSystemInput() {
		inputMap = new HashMap<>();
	}
	
	public FuzzifiedSystemInput setInputValue(int variableId, IFuzzySet valueSet) {
		inputMap.put(variableId, valueSet);
		return this;
	}
	
	public IFuzzySet getValueForVariable(int variableId) {
		return inputMap.get(variableId);
	}

	@Override
	public Iterator<FuzzyInputValue> iterator() {
		return inputMap.entrySet().stream().map(e -> new FuzzyInputValue(e.getKey(),e.getValue())).iterator();
	}
	
}
