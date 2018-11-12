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

		done = facts.stream().noneMatch(List::isEmpty);
	}

	public List<AtomKey> next() {
		List<AtomKey> nextTuple = null;
		if (!done) {
			nextTuple = new ArrayList<>(facts.size());
			for (int i = 0; i < facts.size(); i++) {
				int nextPos = nextFactsPos.get(i).get();
				nextTuple.add(facts.get(i).get(nextPos));
			}
			prepareNext(size - 1);
		}
		return nextTuple;
	}

	public void registerFail(int pos) {
		prepareNext(pos);
	}

	private void prepareNext(int i) {
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
