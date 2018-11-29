package edu.comp6591.problog.engine.naive;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.datastructure.CandidateTupleGenerator;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.engine.ProblogEngineBase;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Naive algorithm to process the Problog database
 */
public class ProblogNaiveEngine extends ProblogEngineBase {

	public ProblogNaiveEngine(IProblogProgram program) {
		super(program);
	}

	/**
	 * Initialize the engine with a set of clauses and calculate fixpoint
	 * 
	 * @param program
	 * @throws ProblogValidationException
	 */
	@Override
	public void init(IProblogProgram program) throws ProblogValidationException {
		if (program == null) {
			throw new IllegalArgumentException("Program cannot be null");
		}
		
		long start = System.currentTimeMillis();
		evaluate();
		long end = System.currentTimeMillis();
		stats.DurationMS = end - start;
	}

	private void evaluate() {
		this.factsRepo = new FactsRepository(initEDBFacts(program.getInitialFacts()));
		this.stats.EDBSize = this.factsRepo.getAllFacts().size();
		
		Set<Atom> factsWithNewValuations = new HashSet<>();
		do {
			this.factsRepo.lock(); // Prevents inconsistent state, defensive measure.
			Map<Atom, Double> newFactsValuations = infer();
			this.factsRepo.unlock();
			// Update the set of atoms with new better valuations.
			factsWithNewValuations.clear();
			for (Entry<Atom, Double> factEntry : newFactsValuations.entrySet()) {
				Double oldValue = factsRepo.getValuation(factEntry.getKey());
				if (oldValue == null || factEntry.getValue() > oldValue) {
					factsWithNewValuations.add(factEntry.getKey());
				}
			}
			factsRepo.putAllFactValuations(newFactsValuations);
			stats.Iterations++;
		} while (!factsWithNewValuations.isEmpty());
		this.stats.IDBSize = this.factsRepo.getAllFacts().size() - this.stats.EDBSize;
	}

	/**
	 * Generates all ground facts and certainty from the set of rules and ground
	 * facts that can be inferred in one step from the given list of facts
	 * 
	 * 
	 * @return Map of ground facts and their certainties found in this iteration.
	 */
	private Map<Atom, Double> infer() {
		ListMultimap<Atom, Double> certaintyBags = LinkedListMultimap.create();

		for (Clause rule : program.getRules()) {
			CandidateTupleGenerator generator = new CandidateTupleGenerator(rule, factsRepo);

			ProblogNaiveClauseEvaluator evaluator = new ProblogNaiveClauseEvaluator(rule, factsRepo, generator);
			evaluator.evaluate();
			certaintyBags.putAll(evaluator.getEvaluationResult());
		}

		return combineGroundFacts(certaintyBags);
	}
}
