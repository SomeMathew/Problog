package edu.comp6591.problog.engine.naive;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.datastructure.CandidateTupleGenerator;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.engine.ProblogEngineBase;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * Naive algorithm to process the Problog database
 */
public class ProblogNaiveEngine extends ProblogEngineBase {
	private IProblogProgram program;
	private FactsRepository factsRepo;

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
		this.program = program; // TODO probably change that to a constructor
		evaluate(); // TODO maybe return something ?
	}

	/**
	 * Browse the engine's database and return the result(s) of the given query
	 * 
	 * @param query
	 * @return result(s)
	 */
	@Override
	public Set<Atom> query(Atom query) { // TODO this need to be changed to a list??
		throw new UnsupportedOperationException("ProblogNaiveEngine 'query' method not supported yet.");
	}

	private void evaluate() {
		this.factsRepo = new FactsRepository(initEDBFacts(program.getInitialFacts()));

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

		} while (!factsWithNewValuations.isEmpty());
	}

	/**
	 * Initializes the first combined certainty map for the EDB facts.
	 * 
	 * @param initialFacts
	 * @return Combined certainty for each Atom facts.
	 */
	private ImmutableMap<Atom, Double> initEDBFacts(List<Clause> initialFacts) {
		ListMultimap<Atom, Double> certaintyBags = LinkedListMultimap.create();

		for (Clause c : initialFacts) {
			certaintyBags.put(c.getHead(), c.getCertainty());
		}

		return combineGroundFacts(certaintyBags);
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

	@Override
	public Map<Atom, Double> getComputedDatabase() {
		return factsRepo.getAllFacts();
	}

}
