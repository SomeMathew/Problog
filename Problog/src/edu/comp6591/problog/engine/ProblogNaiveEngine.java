package edu.comp6591.problog.engine;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.ITerm;
import edu.comp6591.problog.ast.TermVisitor;
import edu.comp6591.problog.datastructure.CandidateTupleGenerator;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;

import java.util.HashSet;
import java.util.Iterator;
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
	private FactsRepository facts;

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
		this.facts = new FactsRepository(initEDBFacts(program.getInitialFacts()));

		Set<Atom> newDerivedFacts = new HashSet<>();
		do {
			Map<Atom, Double> newFactsCertainty = infer();

			// Update newDerivedFacts and the factsByPredicate Index.
			newDerivedFacts.clear();
			for (Entry<Atom, Double> factEntry : newFactsCertainty.entrySet()) {
				Double oldValue = facts.getCertainty(factEntry.getKey());
				if (oldValue == null || factEntry.getValue() > oldValue) {
					newDerivedFacts.add(factEntry.getKey());
				}
			}
			facts.putAllIDBFacts(newFactsCertainty);

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
			CandidateTupleGenerator generator = new CandidateTupleGenerator(rule, facts);

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

	private ImmutableMap<Atom, Double> combineGroundFacts(ListMultimap<Atom, Double> certaintyBags) {
		ImmutableMap.Builder<Atom, Double> builder = new ImmutableMap.Builder<>();

		for (Entry<Atom, List<Double>> bagEntry : Multimaps.asMap(certaintyBags).entrySet()) {
			builder.put(bagEntry.getKey(), disjunction(bagEntry.getValue()));
		}
		return builder.build();
	}

	private Double computeHeadCertainty(Clause clause, List<Atom> factInstantiation) {
		// The conjunction function has greatest element if empty
		Double conjunction = GREATEST_ELEMENT;
		List<Double> bodyCertainties = factInstantiation.stream().map(facts::getCertainty).collect(toList());
		conjunction = conjunction(bodyCertainties);

		return propagation(conjunction, clause.getCertainty());
	}

	/**
	 * Produce a ground fact from applying Elementary Production to a rule and
	 * candidate ground fact.
	 * 
	 * @param generator
	 * @param rule
	 * @param candidateGroundFacts
	 * @return
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

	/**
	 * Try to unify the array of terms, this will update the Unifier.
	 * 
	 * @param mgu
	 * @param ruleTerms
	 * @param factTerms
	 * @return True if it could successfully unify the terms.
	 */
	private boolean unifyArgs(UnionFindBasedUnifier mgu, List<ITerm> ruleTerms, List<ITerm> factTerms) {
		if (mgu == null || ruleTerms.size() != factTerms.size()) {
			throw new IllegalArgumentException("This won't unify ->  rule: " + ruleTerms + ", fact: " + factTerms);
		}
		boolean unifies = true;
		Iterator<ITerm> iterRule = ruleTerms.iterator();
		Iterator<ITerm> iterFact = factTerms.iterator();

		while (unifies && iterRule.hasNext() && iterFact.hasNext()) {
			ITerm ruleTerm = iterRule.next();
			ITerm factTerm = iterFact.next();
			TermVisitor<Boolean> tv = TermVisitor.build((var) -> mgu.unify(var, factTerm),
					(cons) -> cons.equals(factTerm));
			unifies = ruleTerm.accept(tv);
		}
		return unifies;
	}

	@Override
	public Map<Atom, Double> getComputedDatabase() {
		return facts.getAllFacts();
	}

}
