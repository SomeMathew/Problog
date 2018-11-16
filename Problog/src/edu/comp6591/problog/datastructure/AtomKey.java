package edu.comp6591.problog.datastructure;

import java.util.Arrays;
import java.util.StringJoiner;

import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Term;

public class AtomKey {
	private PredicateSym pred;
	private Term[] args;

	public AtomKey(PositiveAtom atom) {
		pred = atom.getPred();
		args = atom.getArgs();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(args);
		result = prime * result + ((pred == null) ? 0 : pred.hashCode());
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
		if (pred == null) {
			if (other.pred != null)
				return false;
		} else if (!pred.equals(other.pred))
			return false;
		return true;
	}

	public PredicateSym getPred() {
		return pred;
	}

	public Term[] getArgs() {
		return args;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", pred + "(", ")");
		for (Term x : args) {
			sj.add(x.toString());
		}
		return sj.toString();
	}

}
