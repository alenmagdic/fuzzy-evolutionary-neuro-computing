package hr.fer.zemris.fuzzy.controlsystem.inputs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hr.fer.zemris.fuzzy.controlsystem.inputs.SystemInput.InputValue;

public class SystemInput implements Iterable<InputValue> {
	private Map<Integer,Integer> inputMap;
	
	class InputValue {
		int variableId;
		int value;
		public InputValue(int variableId, int value) {
			super();
			this.variableId = variableId;
			this.value = value;
		}
		
	}
	
	public SystemInput() {
		inputMap = new HashMap<>();
	}
	
	public SystemInput setInputValue(int variableId, int value) {
		inputMap.put(variableId, value);
		return this;
	}
	
	public int getValueForVariable(int variableId) {
		return inputMap.get(variableId);
	}

	@Override
	public Iterator<InputValue> iterator() {
		return inputMap.entrySet().stream().map(e -> new InputValue(e.getKey(),e.getValue())).iterator();
	}
	
	
	
	
}
