package edu.comp6591.problog.engine;

import abcdatalog.ast.Clause;
import abcdatalog.ast.HeadHelpers;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Premise;
import abcdatalog.ast.Term;
import abcdatalog.ast.visitors.HeadVisitor;
import abcdatalog.ast.visitors.HeadVisitorBuilder;
import abcdatalog.util.substitution.UnionFindBasedUnifier;
import edu.comp6591.problog.datastructure.AtomKey;
import edu.comp6591.problog.datastructure.FactsTupleGenerator;
import edu.comp6591.problog.validation.ProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import edu.comp6591.problog.validation.ProblogValidator.ValidProblogClause;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

import java.util.Arrays;

/**
 * Naive algorithm to process the Problog database
 */
public class ProblogNaiveEngine extends ProblogEngineBase {

	private ProblogProgram program;
	private Map<PredicateSym, Set<AtomKey>> factsByPredicate;

	/**
	 * Initialize the engine with a set of clauses and calculate fixpoint
	 * 
	 * @param program
	 * @throws ProblogValidationException
	 */
	@Override
	public void init(ProblogProgram program) throws ProblogValidationException {
		if (program == null) {
			throw new IllegalArgumentException("Program cannot be null");
		}
		this.program = program; // TODO probably change that to a constructor
		this.factsByPredicate = new HashMap<>();
//		this.factsIndex = new FactsIndex();
		evaluate();
	}

	/**
	 * Browse the engine's database and return the result(s) of the given query
	 * 
	 * @param query
	 * @return result(s)
	 */
	@Override
	public Set<PositiveAtom> query(PositiveAtom query) { // TODO this need to be changed to a list??
		throw new UnsupportedOperationException("ProblogNaiveEngine 'query' method not supported yet.");
	}

	private ProblogEvaluationResult evaluate() {
		Map<AtomKey, Double> iterationCombinedCertainty = initEDBFacts(program.getInitialFacts());
		Set<AtomKey> newDerivedFacts = iterationCombinedCertainty.keySet();
		factsByPredicate = newDerivedFacts.stream().collect(groupingBy((key) -> key.getPred(), Collectors.toSet()));

		while (!newDerivedFacts.isEmpty()) {
			newDerivedFacts = infer(iterationCombinedCertainty);
		}

		// TODO finish this
		return null;
	}

	/**
	 * Initializes the first combined certainty map for the EDB facts.
	 * 
	 * @param initialFacts
	 * @return Combined certainty for each Atom facts.
	 */
	private Map<AtomKey, Double> initEDBFacts(List<ValidProblogClause> initialFacts) {
		HeadVisitor<Void, AtomKey> keyMappingVisitor = new HeadVisitorBuilder<Void, AtomKey>()
				.onPositiveAtom((atom, nothing) -> new AtomKey(atom)).orCrash();

		Map<AtomKey, List<Double>> certaintyBags = initialFacts.stream()
				.collect(groupingBy(clause -> clause.getHead().accept(keyMappingVisitor, null), HashMap::new,
						mapping(ValidProblogClause::getCertainty, toList())));

		Map<AtomKey, Double> combinedCertainty = new HashMap<>();

		certaintyBags.entrySet().stream()
				.forEach((entry) -> combinedCertainty.put(entry.getKey(), disjunction(entry.getValue())));

		return combinedCertainty;
	}

	/**
	 * Generates all ground facts and certainty from the set of rules and ground
	 * facts that can be inferred in one step.
	 * 
	 * 
	 * @param lastDerivedFacts  Set of Atoms that were last derived from the
	 *                          previous iteration
	 * @param combinedCertainty The combined certainty from last iteration.
	 * @return New set of atomkey that derived better certainty.
	 */
	private Set<AtomKey> infer(Map<AtomKey, Double> certainty) {
		Map<AtomKey, List<Double>> certaintyBags = new HashMap<>();

		for (ValidProblogClause rule : program.getRules()) {
			FactsTupleGenerator generator = new FactsTupleGenerator((Clause) rule, factsByPredicate);

			// TODO implement an hasNext...
			List<AtomKey> factInstantiation = generator.next();

			while (factInstantiation != null) {
				PositiveAtom groundFact = produce(generator, rule, factInstantiation);

				// Combine the certainty If we found a new fact
				// TODO encapsulate this
				if (groundFact != null) {
					AtomKey newFact = new AtomKey(groundFact);

					List<Double> bag = certaintyBags.get(newFact);
					if (bag == null) {
						bag = new LinkedList<>();
						certaintyBags.put(newFact, bag);
					}
					List<Double> bodyCertainties = factInstantiation.stream().map(certainty::get)
							.collect(Collectors.toList());
					Double conjunction = conjunction(bodyCertainties);
					Double propagation = propagation(Arrays.asList(rule.getCertainty(), conjunction));
					bag.add(propagation);
				}
				factInstantiation = generator.next();
			}
		}

		Map<AtomKey, Double> newCertainty = new HashMap<>();
		certaintyBags.entrySet().stream()
				.forEach((entry) -> newCertainty.put(entry.getKey(), disjunction(entry.getValue())));

		Set<AtomKey> newBetterFacts = newCertainty.entrySet().stream()
				.filter((entry) -> entry.getValue() > certainty.get(entry.getKey())).map(Entry::getKey)
				.collect(Collectors.toCollection(HashSet::new));
		return newBetterFacts;
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

		int i = 0;
		UnionFindBasedUnifier mgu = null;
		while (i < ruleBody.size()) {
			PositiveAtom nextRuleAtom = (PositiveAtom) ruleBody.get(i);
			Term[] nextRuleTerm;
			if (mgu == null) {
				nextRuleTerm = nextRuleAtom.getArgs();
			} else {
				nextRuleTerm = mgu.apply(nextRuleAtom.getArgs());
			}

			mgu = (UnionFindBasedUnifier) UnionFindBasedUnifier.fromTerms(candidateGroundFacts.get(i).getArgs(),
					nextRuleTerm);

			if (mgu != null) {
				i++;
			} else {
				break;
			}
		}

		if (mgu == null) {
			generator.registerFail(i);
			return null;
		} else {
			PositiveAtom headAtom = HeadHelpers.forcePositiveAtom(rule.getHead());
			return PositiveAtom.create(headAtom.getPred(), mgu.apply(headAtom.getArgs()));
		}
	}
}
