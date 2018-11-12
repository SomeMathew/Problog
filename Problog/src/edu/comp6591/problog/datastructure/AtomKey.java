package edu.comp6591.problog.datastructure;

import java.util.Arrays;

import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Term;

public class AtomKey {
	private PredicateSym predicateSymb;
	private Term[] args;
	
	public AtomKey(PositiveAtom atom) {
		predicateSymb = atom.getPred();
		args = atom.getArgs();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(args);
		result = prime * result + ((predicateSymb == null) ? 0 : predicateSymb.hashCode());
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
		AtomKey other = (AtomKey) obj;
		if (!Arrays.equals(args, other.args))
			return false;
		if (predicateSymb == null) {
			if (other.predicateSymb != null)
				return false;
		} else if (!predicateSymb.equals(other.predicateSymb))
			return false;
		return true;
	}
}
