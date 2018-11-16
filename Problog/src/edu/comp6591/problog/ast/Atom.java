package edu.comp6591.problog.ast;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static java.util.stream.Collectors.*;

import com.google.common.collect.ImmutableList;

/**
 * Immutable type representing an atom in a problog program.
 * 
 * Atoms are considered equal if they have the same predicate and arguments.
 */
public class Atom {
	private final Predicate pred;
	private final ImmutableList<ITerm> args;
	private final boolean isGround;

	public Atom(Predicate pred, List<ITerm> args) {
		this.pred = pred;
		this.args = ImmutableList.copyOf(args);
		isGround = this.args.stream().allMatch(t -> t instanceof Constant);
	}

	public Set<Variable> getVariables() {
		return args.stream().filter((term) -> term instanceof Variable).map((term) -> (Variable) term).collect(toSet());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((args == null) ? 0 : args.hashCode());
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
		Atom other = (Atom) obj;
		if (args == null) {
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (pred == null) {
			if (other.pred != null)
				return false;
		} else if (!pred.equals(other.pred))
			return false;
		return true;
	}

	public boolean isGround() {
		return isGround;
	}

	public Predicate getPred() {
		return pred;
	}

	public ImmutableList<ITerm> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", pred.getSymbol() + "(", ")");
		for (ITerm x : args) {
			sj.add(x.toString());
		}
		return sj.toString();
	}

}
