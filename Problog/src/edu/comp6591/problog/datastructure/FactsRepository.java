package edu.comp6591.problog.datastructure;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.SetMultimap;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Predicate;

public class FactsRepository {

	private SetMultimap<Predicate, Atom> factsAtomIndex;
	private ImmutableMap<Atom, Double> edbValuations;
	private Map<Atom, Double> factsValuation;
	private volatile boolean settersLocked = false;

	public FactsRepository(ImmutableMap<Atom, Double> edbFacts) {
		this.factsAtomIndex = HashMultimap.create();
		factsValuation = new HashMap<>();
		setEDBValuations(edbFacts);
	}

	public synchronized void lock() {
		settersLocked = true;
	}

	public synchronized void unlock() {
		settersLocked = false;
	}

	private void validateLock() throws IllegalAccessError {
		if (settersLocked) {
			throw new IllegalAccessError("Object is locked for write for the iteration.");
		}
	}

	private void setEDBValuations(ImmutableMap<Atom, Double> edbFacts) {
		validateLock();
		this.edbValuations = edbFacts;
		edbFacts.forEach((atom, certainty) -> {
			factsAtomIndex.put(atom.getPred(), atom);
			factsValuation.put(atom, certainty);
		});
	}

	public synchronized void putFactValuation(Atom atom, Double certainty) {
		validateLock();
		this.factsAtomIndex.put(atom.getPred(), atom);
		this.factsValuation.put(atom, certainty);
	}

	public synchronized void putAllFactValuations(Map<Atom, Double> facts) {
		validateLock();
		factsValuation.putAll(facts);
		facts.keySet().forEach((atom) -> factsAtomIndex.put(atom.getPred(), atom));
	}

	public Double getValuation(Atom atom) {
		return factsValuation.get(atom);
	}

	public ImmutableList<Atom> getAtoms(Predicate pred) {
		return ImmutableList.copyOf(factsAtomIndex.get(pred));
	}

	public ImmutableMap<Atom, Double> getAllFacts() {
		return ImmutableMap.copyOf(this.factsValuation);
	}
}
