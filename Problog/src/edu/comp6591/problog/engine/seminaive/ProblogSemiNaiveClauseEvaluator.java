package edu.comp6591.problog.engine.seminaive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.datastructure.ICandidateTupleGenerator;
import edu.comp6591.problog.datastructure.MultisetHashMap;
import edu.comp6591.problog.engine.ProblogClauseEvaluator;

public class ProblogSemiNaiveClauseEvaluator extends ProblogClauseEvaluator {
	private MultisetHashMap<Atom, Double> valuationBags;
	private Map<Clause, Double> ruleInstanceValuations;
	private Map<Clause, Double> newRuleInstanceValuations;

	public ProblogSemiNaiveClauseEvaluator(Clause rule, FactsRepository factsRepo, ICandidateTupleGenerator generator,
			MultisetHashMap<Atom, Double> valuationBags, Map<Clause, Double> ruleInstanceValuations) {
		super(rule, factsRepo, generator);
		this.valuationBags = valuationBags;
		this.ruleInstanceValuations = ruleInstanceValuations;
		this.newRuleInstanceValuations = new HashMap<>();
	}

	@Override
	protected void newGroundFactAction(Atom groundFact, List<Atom> candidateInstance) {
		Clause ruleGroundInstance = new Clause(groundFact, candidateInstance, super.rule.getCertainty());
		valuationBags.remove(groundFact, ruleInstanceValuations.get(ruleGroundInstance));

		Double valuation = computeHeadCertainty(super.rule, candidateInstance);
		valuationBags.put(groundFact, valuation);

		newRuleInstanceValuations.put(ruleGroundInstance, valuation);
	}

	@Override
	protected void completeAction() {
		// Not needed
		return;
	}

	public Map<Clause, Double> getEvaluationResult() {
		return newRuleInstanceValuations;
	}
}
