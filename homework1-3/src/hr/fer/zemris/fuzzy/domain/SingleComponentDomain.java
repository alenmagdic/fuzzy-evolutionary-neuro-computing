package hr.fer.zemris.fuzzy.domain;

public abstract class SingleComponentDomain extends Domain {

	@Override
	public IDomain getComponent(int componentIndex) {
		return this;
	}

	@Override
	public int getNumberOfComponents() {
		return 1;
	}
}
