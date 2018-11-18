package edu.comp6591.problog.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Predicate;

public class CandidateTupleGenerator {
	private List<List<Atom>> facts;
	private List<MutableInteger> nextFactsPos;
	private List<Integer> restrictCount;
	private boolean done;
	private int size;
	private boolean failureRegistered = false;
	private int failurePosition = -1;

	/**
	 * Initializes a new CandidateTupleGenerator for the rule with the possible
	 * ground facts.
	 * 
	 * @param rule
	 * @param factsRepo
	 */
	public CandidateTupleGenerator(Clause rule, FactsRepository factsRepo) {
		this.size = rule.getBody().size();
		this.facts = new ArrayList<>(size);
		nextFactsPos = new ArrayList<>(size);
		List<Atom> body = rule.getBody();

		for (Atom atom : body) {
			List<Atom> candidateFacts = factsRepo.getAtomsAsList(atom.getPred());
			facts.add(candidateFacts);
			nextFactsPos.add(new MutableInteger(0));
		}

		done = facts.isEmpty() || facts.stream().anyMatch(Collection::isEmpty);
	}

	/**
	 * Initializes a new CandidateTupleGenerator with a restriction on the possible
	 * candidate tuple to only the possible tuple with a member in the
	 * restrictedAtom set.
	 * 
	 * @param rule
	 * @param factsRepo
	 * @param restrictedAtom
	 */
	public CandidateTupleGenerator(Clause rule, FactsRepository factsRepo,
			SetMultimap<Predicate, Atom> restrictedAtom) {
		// TODO stub, currently not active
		this(rule, factsRepo);
//		List<Set<Atom>> restrictions = findRestrictedAtoms(rule.getBody(), restrictedAtom);
//
//		this.size = rule.getBody().size();
//		this.facts = new ArrayList<>(size);
//		nextFactsPos = new ArrayList<>(size);
//		this.restrictCount = new ArrayList<>(size);
//
//		Iterator<Atom> bodyIter = rule.getBody().iterator();
//		Iterator<Set<Atom>> restrictIter = restrictions.iterator();
//		while (bodyIter.hasNext() && restrictIter.hasNext()) {
//			Atom atom = bodyIter.next();
//			Set<Atom> candidateFacts = factsRepo.getAtomsAsSet(atom.getPred());
//
//			Set<Atom> restrictSet = restrictIter.next();
//
//			// Builds the fact such that the restricted atoms are always first
//			ImmutableList.Builder<Atom> builder = new ImmutableList.Builder<Atom>();
//			builder.addAll(restrictSet);
//			builder.addAll(Sets.difference(candidateFacts, restrictSet));
//			facts.add(builder.build());
//
//			this.restrictCount.add(restrictSet.size());
//			nextFactsPos.add(new MutableInteger(0));
//		}
//
//		done = facts.isEmpty() || facts.stream().anyMatch(Collection::isEmpty);
	}

	/**
	 * Build a table of the predicate found in the restrictedAtom set for each
	 * position of the body.
	 * 
	 * @param body
	 * @param restrictedAtom
	 * @return
	 */
	private List<Set<Atom>> findRestrictedAtoms(List<Atom> body, SetMultimap<Predicate, Atom> restrictedAtom) {
		List<Set<Atom>> restriction = new ArrayList<>();
		for (Atom atom : body) {
			restriction.add(restrictedAtom.get(atom.getPred()));
		}
		return restriction;
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
			for (int i = 0; i < this.size; i++) {
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
