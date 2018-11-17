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
	private ImmutableMap<Atom, Double> edbFacts;
	private Map<Atom, Double> dbFacts;
	private volatile boolean settersLocked = false;

	public FactsRepository(ImmutableMap<Atom, Double> edbFacts) {
		this.factsAtomIndex = HashMultimap.create();
		dbFacts = new HashMap<>();
		setEDBFacts(edbFacts);
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

	private void setEDBFacts(ImmutableMap<Atom, Double> edbFacts) {
		validateLock();
		this.edbFacts = edbFacts;
		edbFacts.forEach((atom, certainty) -> {
			factsAtomIndex.put(atom.getPred(), atom);
			dbFacts.put(atom, certainty);
		});
	}

	public synchronized void putIDBFact(Atom atom, Double certainty) {
		validateLock();
		this.factsAtomIndex.put(atom.getPred(), atom);
		this.dbFacts.put(atom, certainty);
	}

	public synchronized void putAllIDBFacts(Map<Atom, Double> facts) {
		validateLock();
		dbFacts.putAll(facts);
		facts.keySet().forEach((atom) -> factsAtomIndex.put(atom.getPred(), atom));
	}

	public Double getCertainty(Atom atom) {
		return dbFacts.get(atom);
	}

	public ImmutableList<Atom> getAtoms(Predicate pred) {
		return ImmutableList.copyOf(factsAtomIndex.get(pred));
	}

	public ImmutableMap<Atom, Double> getAllFacts() {
		return ImmutableMap.copyOf(this.dbFacts);
	}
}
