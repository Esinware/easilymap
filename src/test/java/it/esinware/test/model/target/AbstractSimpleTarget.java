package it.esinware.test.model.target;

public abstract class AbstractSimpleTarget {

	private String a;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	@Override
	public String toString() {
		return "AbstractSimpleTarget [a=" + a + "]";
	}
}