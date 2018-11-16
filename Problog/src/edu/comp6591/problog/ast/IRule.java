package edu.comp6591.problog.ast;

import com.google.common.collect.ImmutableList;

public interface IRule {
	public Atom getHead();

	public ImmutableList<Atom> getBody();

	public Certainty getCertainty();

	public boolean isGround();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

	@Override
	String toString();

}
