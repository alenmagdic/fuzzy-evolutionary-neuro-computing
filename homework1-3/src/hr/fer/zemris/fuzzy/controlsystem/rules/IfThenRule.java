package hr.fer.zemris.fuzzy.controlsystem.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hr.fer.zemris.fuzzy.domain.IDomain;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;

public class IfThenRule {
	private List<IFuzzySet> antecedentSets;
	private IFuzzySet consequentSet;
	private Map<Integer,Integer> variableIdToComponentIndex; //mapira identifikator varijable na komponentu skupa koja predstavlja tu varijablu
	private int consequentVariableId;
	
	public IfThenRule() {
		variableIdToComponentIndex = new HashMap<>();
		antecedentSets = new ArrayList<>();
	}

	public IfThenRule ifIs(int variableId,IFuzzySet predicateSet) {
		return andIf(variableId,predicateSet);
	}
	
	public IfThenRule andIf(int variableId,IFuzzySet predicateSet) {
		variableIdToComponentIndex.put(variableId, antecedentSets.size()); //biljezenje indexa komponente na kojem ce se nalaziti doticna varijabla
		antecedentSets.add(predicateSet);
		return this;
	}
	
	public IfThenRule then(IFuzzySet consequentSet) {		
		this.consequentSet = consequentSet;
		return this;
	}
		
	public IFuzzySet getComponentForVariable(int variableId)  {
		Integer index = variableIdToComponentIndex.get(variableId);
		return index==null?null:antecedentSets.get(index);
	}
	
	public int getConsequentVariableId() {
		return consequentVariableId;
	}
	
	public IDomain getConsequentDomain() {
		return consequentSet.getDomain();
	}
	
	public IFuzzySet getConsequentSet() {
		return consequentSet;
	}
	
}
