package edu.comp6591.problog.ast;

/**
 * Represents a Predicate in a problog program, symbol and arity.
 * 
 * Two predicate are equal of they have the same symbol and arity.
 */
public class Predicate {
	private final String symbol;
	private final int arity;

	public Predicate(String symbol, int arity) {
		this.symbol = symbol;
		this.arity = arity;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getArity() {
		return arity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arity;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		Predicate other = (Predicate) obj;
		if (arity != other.arity)
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Predicate [");
		builder.append(symbol);
		builder.append(", ");
		builder.append(arity);
		builder.append("]");
		return builder.toString();
	}

}
