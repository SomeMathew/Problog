package edu.comp6591.problog.datastructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import abcdatalog.ast.Constant;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Term;

/**
 * Data structure to index Predicates and the position of constants in a set of
 * facts.
 */
public class FactsIndex {
	private Map<PredicateSym, Map<ConstantPosition, Set<AtomKey>>> index;

	public FactsIndex() {
		this.index = new HashMap<>();
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
			facts = constantPositions.stream().filter(consPosMap::containsKey)
					.flatMap((key) -> consPosMap.get(key).stream()).collect(Collectors.toSet());
		}
		return facts;
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
