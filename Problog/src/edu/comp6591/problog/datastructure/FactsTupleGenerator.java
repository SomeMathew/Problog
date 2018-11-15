package edu.comp6591.problog.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Premise;

public class FactsTupleGenerator {
	private List<List<AtomKey>> facts;
	private List<MutableInteger> nextFactsPos;
	private boolean done;
	private int size;
	private boolean failureRegistered = false;
	private int failurePosition = -1;

	public FactsTupleGenerator(Clause rule, Map<PredicateSym, Set<AtomKey>> factsByPredicate) {
		this.size = rule.getBody().size();
		facts = new ArrayList<>(size);
		nextFactsPos = new ArrayList<>(size);
		List<Premise> premises = rule.getBody();

		premises.stream().map((premise) -> {
			PredicateSym pred = ((PositiveAtom) premise).getPred();
			return factsByPredicate.getOrDefault(pred, new TreeSet<>());
		}).map(ArrayList::new).forEach((factList) -> {
			facts.add(factList);
			nextFactsPos.add(new MutableInteger(0));
		});

		done = facts.isEmpty() || facts.stream().anyMatch(List::isEmpty);
	}

	/**
	 * Get the next list of candidate facts for unification.
	 * 
	 * @return
	 */
	public List<AtomKey> next() {
		List<AtomKey> nextTuple = null;
		if (!done) {
			nextTuple = new ArrayList<>(facts.size());
			for (int i = 0; i < facts.size(); i++) {
				int nextPos = nextFactsPos.get(i).get();
				nextTuple.add(facts.get(i).get(nextPos));
			}
			prepareNext();
		}
		return nextTuple;
	}

	/**
	 * Registers a failure from a given predicate position in the rule generated.
	 * 
	 * This will skip all child of this given generated fact that caused a failure
	 * in unification. Trims the space of possibility.
	 * 
	 * @param pos
	 */
	public void registerFail(int pos) {
		failureRegistered = true;
		failurePosition = pos;
	}

	private void prepareNext() {
		int i;
		// Select the next position to increment based on any registered unification
		// failure.
		if (failureRegistered) {
			i = failurePosition;
			failureRegistered = false;
		} else {
			i = size - 1;
		}

		while (i >= 0 && nextFactsPos.get(i).incrementAndGet() >= facts.get(i).size()) {
			i--;
		}

		if (i < 0) {
			this.done = true;
		} else {
			for (int k = i + 1; k < size; k++) {
				nextFactsPos.get(k).set(0);
			}
		}
	}
}
