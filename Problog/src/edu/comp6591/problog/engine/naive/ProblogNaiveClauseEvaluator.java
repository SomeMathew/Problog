package edu.comp6591.problog.engine.naive;

import java.util.List;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.datastructure.CandidateTupleGenerator;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.engine.ProblogClauseEvaluator;

public class ProblogNaiveClauseEvaluator extends ProblogClauseEvaluator {
	private ListMultimap<Atom, Double> certaintyBags;

	public ProblogNaiveClauseEvaluator(Clause rule, FactsRepository factsRepo, CandidateTupleGenerator generator) {
		super(rule, factsRepo, generator);
		certaintyBags = LinkedListMultimap.create();
	}

	@Override
	protected void newGroundFactAction(Atom groundFact, List<Atom> candidateInstance) {
		Double headCertainty = computeHeadCertainty(rule, candidateInstance);
		certaintyBags.put(groundFact, headCertainty);
	}

	@Override
	protected void completeAction() {
		// Not needed
		return;
	}

	public ListMultimap<Atom, Double> getEvaluationResult() {
		return certaintyBags;
	}
}
