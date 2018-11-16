package edu.comp6591.problog.ast;

public class Certainty implements Comparable<Certainty> {
	public static final double LEAST_ELEMENT = 0;
	public static final double GREATEST_ELEMENT = 1;

	private final double value;

	/**
	 * Build a certainty with a value in [0,1].
	 * 
	 * @param value
	 */
	public Certainty(double value) {
		if (value < LEAST_ELEMENT || value > GREATEST_ELEMENT) {
			throw new IllegalArgumentException("Certainty must be in [0,1]");
		}
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Certainty other = (Certainty) obj;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}

	@Override
	public int compareTo(Certainty c) {
		return Double.compare(this.value, c.value);
	}

}
