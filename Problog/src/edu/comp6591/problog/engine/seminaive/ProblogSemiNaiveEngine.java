package edu.comp6591.problog.engine.seminaive;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Predicate;
import edu.comp6591.problog.datastructure.CandidateTupleGenerator;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.datastructure.MultisetHashMap;
import edu.comp6591.problog.engine.ProblogEngineBase;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;

public class ProblogSemiNaiveEngine extends ProblogEngineBase {
	private IProblogProgram program;
	private FactsRepository factsRepo;
	private SetMultimap<Predicate, Clause> ruleIndex;
	private MultisetHashMap<Atom, Double> valuationBags;

	@Override
	public void init(IProblogProgram program) throws ProblogValidationException {
		if (program == null) {
			throw new IllegalArgumentException("Program cannot be null");
		}
		this.program = program; // TODO probably change that to a constructor
		buildRuleIndex(this.program.getRules());
		valuationBags = new MultisetHashMap<>();

		evaluate(); // TODO maybe return something ?
	}

	private void buildRuleIndex(List<Clause> rules) {
		ImmutableSetMultimap.Builder<Predicate, Clause> builder = new ImmutableSetMultimap.Builder<>();
		for (Clause clause : rules) {
			for (Atom atom : clause.getBody()) {
				builder.put(atom.getPred(), clause);
			}
		}
		this.ruleIndex = builder.build();
	}

	private void evaluate() {
		ImmutableMap<Atom, Double> edbFacts = initEDBFacts(program.getInitialFacts());
		this.factsRepo = new FactsRepository(edbFacts);
		Set<Atom> factsWithNewValuations = new HashSet<>(edbFacts.keySet());
		Map<Clause, Double> ruleInstanceValuation = ImmutableMap.of();

		while (!factsWithNewValuations.isEmpty()) {
			Map<Clause, Double> newRuleInstanceValuations = infer(factsWithNewValuations, ruleInstanceValuation);
			Set<Atom> changedAtom = newRuleInstanceValuations.keySet().stream().map(Clause::getHead)
					.collect(toImmutableSet());

			Map<Atom, Double> newFactsValuation = computeNewValuations(changedAtom);

			factsWithNewValuations.clear();
			for (Entry<Atom, Double> factEntry : newFactsValuation.entrySet()) {
				Double oldValue = factsRepo.getValuation(factEntry.getKey());
				if (oldValue == null || factEntry.getValue() > oldValue) {
					factsWithNewValuations.add(factEntry.getKey());
				}
			}
			// Update the fact repo
			factsRepo.putAllFactValuations(newFactsValuation);

			// Keep iteration ground rule instance valuation
			ruleInstanceValuation = newRuleInstanceValuations; // TODO double check that this is right
		}
	}

	private ImmutableMap<Atom, Double> computeNewValuations(Set<Atom> changedAtom) {
		ImmutableMap.Builder<Atom, Double> newValuationBuilder = new ImmutableMap.Builder<Atom, Double>();
		for (Atom atom : changedAtom) {
			newValuationBuilder.put(atom, disjunction(valuationBags.get(atom)));
		}
		ImmutableMap<Atom, Double> newValuationMap = newValuationBuilder.build();
		return newValuationMap;
	}

	/**
	 * Generates all new ground facts and certainty that can be inferred in one
	 * step.
	 * 
	 * @return
	 */
	private Map<Clause, Double> infer(Set<Atom> factsWithNewValuations, Map<Clause, Double> ruleInstanceValuations) {
		Set<Clause> candidateRules = findCandidateRules(factsWithNewValuations);
		SetMultimap<Predicate, Atom> newFactsIndex = buildAtomIndex(factsWithNewValuations);

		Map<Clause, Double> newRuleInstanceValuations = new HashMap<>();

		for (Clause rule : candidateRules) {
			CandidateTupleGenerator generator = new CandidateTupleGenerator(rule, factsRepo, newFactsIndex);
			ProblogSemiNaiveClauseEvaluator evaluator = new ProblogSemiNaiveClauseEvaluator(rule, factsRepo, generator,
					valuationBags, ruleInstanceValuations);
			evaluator.evaluate();
			newRuleInstanceValuations.putAll(evaluator.getEvaluationResult());
		}

		return newRuleInstanceValuations;
	}

	private Set<Clause> findCandidateRules(Collection<Atom> factsWithNewValuations) {
		// Build candidate rules
		ImmutableSet.Builder<Clause> builder = new ImmutableSet.Builder<>();
		for (Atom atom : factsWithNewValuations) {
			builder.addAll(ruleIndex.get(atom.getPred()));
		}
		Set<Clause> candidateRules = builder.build();
		return candidateRules;
	}

	private SetMultimap<Predicate, Atom> buildAtomIndex(Collection<Atom> factsWithNewValuations) {
		ImmutableSetMultimap.Builder<Predicate, Atom> builder = new ImmutableSetMultimap.Builder<>();
		for (Atom atom : factsWithNewValuations) {
			builder.put(atom.getPred(), atom);
		}
		return builder.build();
	}

	@Override
	public Map<Atom, Double> getComputedDatabase() {
		return factsRepo.getAllFacts();
	}

	@Override
	public Set<Atom> query(Atom query) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method stub");
	}

}
