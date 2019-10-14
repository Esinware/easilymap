package it.esinware.test.model.target;

public class SimpleTarget extends AbstractSimpleTarget {

	private String b;

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "SimpleTarget [b=" + b + ", toString()=" + super.toString() + "]";
	}
}