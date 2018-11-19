package edu.comp6591.problog.datastructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

import edu.comp6591.problog.ast.Constant;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Predicate;
import edu.comp6591.problog.ast.ITerm;
import edu.comp6591.problog.ast.TermVisitor;

/**
 * Data structure to index Predicates and the position of constants in a set of
 * facts.
 */
public class FactsIndex {
	private Map<Predicate, Map<ConstantPosition, Set<Atom>>> index;

	public FactsIndex() {
		this.index = new HashMap<>();
	}

	public void addFacts(Collection<Atom> facts) {
		facts.forEach(this::addFact);
	}

	public void addFact(Atom fact) {
		Map<ConstantPosition, Set<Atom>> consPosMap = index.get(fact.getPred());
		if (consPosMap == null) {
			consPosMap = new HashMap<>();
			index.put(fact.getPred(), consPosMap);
		}

		ITerm[] terms = (ITerm[])fact.getArgs().toArray();
		for (int i = 0; i < terms.length; i++) {
			ConstantPosition key = new ConstantPosition(i, (Constant) terms[i]);

			Set<Atom> factSet = consPosMap.get(key);
			if (factSet == null) {
				factSet = new HashSet<>();
				consPosMap.put(key, factSet);
			}
			factSet.add(fact);
		}
	}

	public Set<Atom> retrieve(Predicate predicate, List<ConstantPosition> constantPositions) {
		Set<Atom> facts = null;
		Map<ConstantPosition, Set<Atom>> consPosMap = index.get(predicate);
		if (consPosMap != null) {

			List<Set<Atom>> factsNonIntersected = constantPositions.stream().filter(consPosMap::containsKey)
					.map((key) -> consPosMap.get(key)).collect(toList());
		}
		return facts;
	}

	public Set<Atom> retrieve(Atom atom) {
		List<ConstantPosition> positions = new LinkedList<>();

		ITerm[] args = (ITerm[])atom.getArgs().toArray();
		TermVisitor<Constant> visitor = TermVisitor.build(null, (cons) -> cons);
		for (int i = 0; i < args.length; i++) {
			Constant c = args[i].accept(visitor);
			if (c != null) {
				positions.add(new ConstantPosition(i, c));
			}
		}

		return retrieve(atom.getPred(), positions);
	}

	public static class ConstantPosition {
		private int position;
		private Constant constant;

		public ConstantPosition(int position, Constant constant) {
			this.position = position;
			this.constant = constant;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((constant == null) ? 0 : constant.hashCode());
			result = prime * result + position;
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
			ConstantPosition other = (ConstantPosition) obj;
			if (constant == null) {
				if (other.constant != null)
					return false;
			} else if (!constant.equals(other.constant))
				return false;
			if (position != other.position)
				return false;
			return true;
		}
	}
}
