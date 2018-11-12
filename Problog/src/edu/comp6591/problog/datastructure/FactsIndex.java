package edu.comp6591.problog.datastructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import abcdatalog.ast.Constant;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Term;
import abcdatalog.ast.visitors.TermVisitor;
import abcdatalog.ast.visitors.TermVisitorBuilder;

/**
 * Data structure to index Predicates and the position of constants in a set of
 * facts.
 */
public class FactsIndex {
	private Map<PredicateSym, Map<ConstantPosition, Set<AtomKey>>> index;

	public FactsIndex() {
		this.index = new HashMap<>();
	}

	public void addFacts(Collection<AtomKey> facts) {
		facts.forEach(this::addFact);
	}

	public void addFact(AtomKey fact) {
		Map<ConstantPosition, Set<AtomKey>> consPosMap = index.get(fact.getPred());
		if (consPosMap == null) {
			consPosMap = new HashMap<>();
			index.put(fact.getPred(), consPosMap);
		}

		Term[] terms = fact.getArgs();
		for (int i = 0; i < terms.length; i++) {
			ConstantPosition key = new ConstantPosition(i, (Constant) terms[i]);

			Set<AtomKey> factSet = consPosMap.get(key);
			if (factSet == null) {
				factSet = new HashSet<>();
				consPosMap.put(key, factSet);
			}
			factSet.add(fact);
		}
	}

	public Set<AtomKey> retrieve(PredicateSym predicate, List<ConstantPosition> constantPositions) {
		Set<AtomKey> facts = null;
		Map<ConstantPosition, Set<AtomKey>> consPosMap = index.get(predicate);
		if (consPosMap != null) {
			
			
			List<Set<AtomKey>> factsNonIntersected = constantPositions.stream().filter(consPosMap::containsKey)
					.map((key) -> consPosMap.get(key)).collect(Collectors.toList());
		}
		return facts;
	}

	public Set<AtomKey> retrieve(PositiveAtom atom) {
		List<ConstantPosition> positions = new LinkedList<>();

		Term[] args = atom.getArgs();
		TermVisitor<Void, Constant> visitor = new TermVisitorBuilder<Void, Constant>()
				.onConstant((cons, unused) -> cons).orNull();
		for (int i = 0; i < args.length; i++) {
			Constant c = args[i].accept(visitor, null);
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
