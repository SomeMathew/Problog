package edu.comp6591.problog.ast;

public interface IFact {
	public Atom getHead();

	public Certainty getCertainty();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

	@Override
	String toString();

}
