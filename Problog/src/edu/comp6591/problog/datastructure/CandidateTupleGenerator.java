package edu.comp6591.problog.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;

public class CandidateTupleGenerator {
	private List<List<Atom>> facts;
	private List<MutableInteger> nextFactsPos;
	private boolean done;
	private int size;
	private boolean failureRegistered = false;
	private int failurePosition = -1;

	public CandidateTupleGenerator(Clause rule, FactsRepository factsRepo) {
		this.size = rule.getBody().size();
		this.facts = new ArrayList<>(size);
		nextFactsPos = new ArrayList<>(size);
		List<Atom> body = rule.getBody();

		for (Atom atom : body) {
			List<Atom> candidateFacts = factsRepo.getAtoms(atom.getPred());
			facts.add(candidateFacts);
			nextFactsPos.add(new MutableInteger(0));
		}

		done = facts.isEmpty() || facts.stream().anyMatch(Collection::isEmpty);
	}

	/**
	 * Get the next list of candidate facts for unification.
	 * 
	 * @return The next tuple of candidate facts, it will be empty if no more
	 *         possible
	 */
	public List<Atom> next() {
		ImmutableList.Builder<Atom> builder = new ImmutableList.Builder<Atom>();
		if (!done) {
			for (int i = 0; i < facts.size(); i++) {
				int nextPos = nextFactsPos.get(i).get();
				builder.add(facts.get(i).get(nextPos));
			}
			prepareNext();
		}
		return builder.build();
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
