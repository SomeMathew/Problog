package edu.comp6591.problog.engine;

import abcdatalog.ast.Clause;
import abcdatalog.ast.Constant;
import abcdatalog.ast.HeadHelpers;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Premise;
import abcdatalog.ast.Term;
import abcdatalog.ast.Variable;
import abcdatalog.util.substitution.UnionFindBasedUnifierOld;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.datastructure.AtomKey;
import edu.comp6591.problog.datastructure.FactsTupleGenerator;
import edu.comp6591.problog.validation.ProblogProgramOLD;
import edu.comp6591.problog.validation.ProblogValidationException;
import edu.comp6591.problog.validation.ProblogValidatorOld.ValidProblogClause;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.stream.Collectors.*;

/**
 * Naive algorithm to process the Problog database
 */
public class ProblogNaiveEngine extends ProblogEngineBase {
	private ProblogProgramOLD program;
	private Map<PredicateSym, Set<AtomKey>> factsIndex;
	private Map<AtomKey, Double> computedDB = null;

	/**
	 * Initialize the engine with a set of clauses and calculate fixpoint
	 * 
	 * @param program
	 * @throws ProblogValidationException
	 */
	@Override
	public void init(ProblogProgramOLD program) throws ProblogValidationException {
		if (program == null) {
			throw new IllegalArgumentException("Program cannot be null");
		}
		this.program = program; // TODO probably change that to a constructor
		this.factsIndex = new HashMap<>();
//		this.factsIndex = new FactsIndex();
		computedDB = evaluate();
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

	private Map<AtomKey, Double> evaluate() {
		Map<AtomKey, Double> lastFactsCertainty = new HashMap<>();
		Set<AtomKey> newDerivedFacts = new HashSet<>();
		factsIndex = new HashMap<>();

		do {
			Map<AtomKey, Double> newFactsCertainty = infer(lastFactsCertainty);

			// Update newDerivedFacts and the factsByPredicate Index.
			newDerivedFacts.clear();
			for (Entry<AtomKey, Double> factEntry : newFactsCertainty.entrySet()) {
				Double oldValue = lastFactsCertainty.get(factEntry.getKey());
				if (oldValue == null || factEntry.getValue() > oldValue) {
					newDerivedFacts.add(factEntry.getKey());
				}
				if (oldValue == null) {
					addToFactsIndex(factEntry);
				}
			}
			lastFactsCertainty = newFactsCertainty;
		} while (!newDerivedFacts.isEmpty());

		return lastFactsCertainty;
	}

	private void addToFactsIndex(Entry<AtomKey, Double> factEntry) {
		PredicateSym pred = factEntry.getKey().getPred();
		Set<AtomKey> factsSet = factsIndex.get(pred);
		if (factsSet == null) {
			factsSet = new HashSet<>();
			factsIndex.put(pred, factsSet);
		}
		factsSet.add(factEntry.getKey());
	}

	/**
	 * Generates all ground facts and certainty from the set of rules and ground
	 * facts that can be inferred in one step from the given list of facts
	 * 
	 * 
	 * @param lastFacts The combined certainties from last iteration for all ground
	 *                  facts.
	 * @return Map of ground facts and their certainties found in this iteration.
	 */
	private Map<AtomKey, Double> infer(Map<AtomKey, Double> lastFacts) {
		Map<AtomKey, List<Double>> certaintyBags = new HashMap<>();

		for (ValidProblogClause clause : program.getAllClauses()) {
			FactsTupleGenerator generator = new FactsTupleGenerator((Clause) clause, factsIndex);

			// TODO implement an hasNext...
			List<AtomKey> factInstantiation = generator.next();

			while (factInstantiation != null || clause.getBody().isEmpty()) {
				PositiveAtom groundFact = null;
				groundFact = produce(generator, clause, factInstantiation);

				// Combine the certainty If we found a new fact
				// TODO encapsulate this
				if (groundFact != null) {
					AtomKey newFact = new AtomKey(groundFact);

					List<Double> bag = certaintyBags.get(newFact);
					if (bag == null) {
						bag = new LinkedList<>();
						certaintyBags.put(newFact, bag);
					}

					// The conjunction function has greatest element if empty
					Double conjunction = GREATEST_ELEMENT;
					if (factInstantiation != null) {
						List<Double> bodyCertainties = factInstantiation.stream().map(lastFacts::get).collect(toList());
						conjunction = conjunction(bodyCertainties);
					}

					Double propagation = propagation(conjunction, clause.getCertainty());
					bag.add(propagation);

					// Break if this was an edb ground fact
					if (clause.getBody().isEmpty()) {
						break;
					}
				}
				factInstantiation = generator.next();
			}
		}

		Map<AtomKey, Double> newCertainties = new HashMap<>();
		for (Entry<AtomKey, List<Double>> bagEntry : certaintyBags.entrySet()) {
			newCertainties.put(bagEntry.getKey(), disjunction(bagEntry.getValue()));
		}

		return newCertainties;
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
	private PositiveAtom produce(FactsTupleGenerator generator, ValidProblogClause rule,
			List<AtomKey> candidateGroundFacts) {
		List<Premise> ruleBody = rule.getBody();

		UnionFindBasedUnifierOld mgu = new UnionFindBasedUnifierOld();
		boolean unifies = true;
		int i;
		for (i = 0; i < ruleBody.size() && unifies; i++) {
			PositiveAtom nextRuleAtom = (PositiveAtom) ruleBody.get(i);
			AtomKey nextFactAtom = candidateGroundFacts.get(i);

			if (!nextRuleAtom.getPred().getSym().equals(nextFactAtom.getPred().getSym())
					|| nextRuleAtom.getPred().getArity() != nextFactAtom.getPred().getArity()) {
				unifies = false;
			} else {
				Term[] ruleTerms = nextRuleAtom.getArgs();
				Term[] factTerms = nextFactAtom.getArgs();

				unifies = unifyArgs(mgu, ruleTerms, factTerms);
			}
		}

		PositiveAtom headAtom = HeadHelpers.forcePositiveAtom(rule.getHead());
		if (!unifies && !headAtom.isGround()) {
			generator.registerFail(i - 1);
			return null;
		} else {
			return PositiveAtom.create(headAtom.getPred(), mgu.apply(headAtom.getArgs()));
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
	private boolean unifyArgs(UnionFindBasedUnifierOld mgu, Term[] ruleTerms, Term[] factTerms) {
		boolean unifies = true;
		for (int t = 0; t < ruleTerms.length && unifies; t++) {
			if (ruleTerms[t] instanceof Variable) {
				unifies = mgu.unify((Variable) ruleTerms[t], factTerms[t]);
			} else if (ruleTerms[t] instanceof Constant) {
				unifies = ((Constant) ruleTerms[t]).equals((Constant) factTerms[t]);
			}
		}
		return unifies;
	}

	@Override
	public Map<AtomKey, Double> getComputedDatabase() {
		return computedDB;
	}

}
