package edu.comp6591.problog.ast;

public interface ITerm {
	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

	@Override
	public String toString();

	public <T> T accept(TermVisitor<T> visitor);
}
