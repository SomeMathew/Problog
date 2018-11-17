package edu.comp6591.problog.engine;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.datastructure.CandidateTupleGenerator;
import edu.comp6591.problog.datastructure.FactsRepository;
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
import com.google.common.collect.Multimaps;

import abcdatalog.util.substitution.UnionFindBasedUnifier;

import static java.util.stream.Collectors.*;

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

		Set<Atom> newDerivedFacts = new HashSet<>();
		do {
			this.factsRepo.lock(); // Prevents inconsistent state, defensive measure.
			Map<Atom, Double> newFactsCertainty = infer();
			this.factsRepo.unlock();
			// Update newDerivedFacts and the factsByPredicate Index.
			newDerivedFacts.clear();
			for (Entry<Atom, Double> factEntry : newFactsCertainty.entrySet()) {
				Double oldValue = factsRepo.getCertainty(factEntry.getKey());
				if (oldValue == null || factEntry.getValue() > oldValue) {
					newDerivedFacts.add(factEntry.getKey());
				}
			}
			factsRepo.putAllIDBFacts(newFactsCertainty);

		} while (!newDerivedFacts.isEmpty());
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

			// TODO implement an hasNext...
			List<Atom> candidate = generator.next();

			while (!candidate.isEmpty()) {
				Atom groundFact = produce(generator, rule, candidate);

				if (groundFact != null) {
					Double headCertainty = computeHeadCertainty(rule, candidate);
					certaintyBags.put(groundFact, headCertainty);
				}
				candidate = generator.next();
			}
		}

		return combineGroundFacts(certaintyBags);
	}

	/**
	 * Combines bags of Certainty with the disjunction function for each atom.
	 * 
	 * @param certaintyBags
	 * @return Map of Atom to combined certainty
	 */
	private ImmutableMap<Atom, Double> combineGroundFacts(ListMultimap<Atom, Double> certaintyBags) {
		ImmutableMap.Builder<Atom, Double> builder = new ImmutableMap.Builder<>();

		for (Entry<Atom, List<Double>> bagEntry : Multimaps.asMap(certaintyBags).entrySet()) {
			builder.put(bagEntry.getKey(), disjunction(bagEntry.getValue()));
		}
		return builder.build();
	}

	/**
	 * Applies the conjunction function to the list of facts and propagate with the
	 * clause certainty parameter.
	 * 
	 * @param rule              Rule to compute the head certainty for
	 * @param factInstantiation List of body facts to retrieve the certainty from
	 * @return Derived Certainty for the rule.
	 */
	private Double computeHeadCertainty(Clause rule, List<Atom> factInstantiation) {
		// The conjunction function has greatest element if empty
		Double conjunction = GREATEST_ELEMENT;
		List<Double> bodyCertainties = factInstantiation.stream().map(factsRepo::getCertainty).collect(toList());
		conjunction = conjunction(bodyCertainties);

		return propagation(conjunction, rule.getCertainty());
	}

	/**
	 * Produce a ground fact from applying Elementary Production to a rule and a
	 * list of candidate ground fact.
	 * 
	 * @param generator            Generator used to generate the candidateFacts
	 * @param rule                 Rule to try unifying
	 * @param candidateGroundFacts Ordered List of candidate Facts for the rule
	 * @return Ground Fact produced from the rule or null if it cannot unify
	 */
	public Atom produce(CandidateTupleGenerator generator, Clause rule, List<Atom> candidateFacts) {
		List<Atom> ruleBody = rule.getBody();

		UnionFindBasedUnifier mgu = new UnionFindBasedUnifier();
		boolean unifies = true;
		int i;
		for (i = 0; i < ruleBody.size() && unifies; i++) {
			Atom nextAtom = ruleBody.get(i);
			Atom nextCandidate = candidateFacts.get(i);

			if (!nextAtom.getPred().getSymbol().equals(nextCandidate.getPred().getSymbol())
					|| nextAtom.getPred().getArity() != nextCandidate.getPred().getArity()) {
				unifies = false;
			} else {
				unifies = unifyArgs(mgu, nextAtom.getArgs(), nextCandidate.getArgs());
			}
		}

		Atom headAtom = rule.getHead();
		if (!unifies && !headAtom.isGround()) {
			generator.registerFail(i - 1);
			return null;
		} else {
			return new Atom(headAtom.getPred(), mgu.apply(headAtom.getArgs()));
		}
	}

	@Override
	public Map<Atom, Double> getComputedDatabase() {
		return factsRepo.getAllFacts();
	}

}
