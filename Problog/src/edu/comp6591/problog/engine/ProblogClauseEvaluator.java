package edu.comp6591.problog.engine;

import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;

import abcdatalog.util.substitution.UnionFindBasedUnifier;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.ITerm;
import edu.comp6591.problog.ast.TermVisitor;
import edu.comp6591.problog.datastructure.FactsRepository;
import edu.comp6591.problog.datastructure.ICandidateTupleGenerator;

public abstract class ProblogClauseEvaluator {
	private ICandidateTupleGenerator generator;
	private boolean evaluated;

	protected Clause rule;
	protected FactsRepository factsRepo;

	protected ProblogClauseEvaluator(Clause rule, FactsRepository factsRepo, ICandidateTupleGenerator generator) {
		this.rule = rule;
		this.factsRepo = factsRepo;
		this.generator = generator;
		this.evaluated = false;
	}

	public boolean evaluate() {
		if (!evaluated) {
			// TODO implement an hasNext...
			List<Atom> candidateInstance = generator.next();

			while (!candidateInstance.isEmpty()) {
				ProductionResult production = produce(this.rule, candidateInstance);
				if (production.isUnified()) {
					Atom groundFact = production.getAtom();
					newGroundFactAction(groundFact, candidateInstance);
				} else {
					generator.registerFail(production.getFailurePosition());
				}
				candidateInstance = generator.next();
			}

			completeAction();

			evaluated = true;
		}
		return evaluated;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	protected abstract void newGroundFactAction(Atom groundFact, List<Atom> candidateInstance);

	protected abstract void completeAction();

	/**
	 * Produce a ground fact from applying Elementary Production to a rule and a
	 * list of candidate ground fact.
	 * 
	 * @param rule                 Rule to try unifying
	 * @param candidateGroundFacts Ordered List of candidate Facts for the rule
	 * @return Ground Fact produced from the rule or error position if it couldn't
	 *         unify
	 */
	private ProductionResult produce(Clause rule, List<Atom> candidateFacts) {
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
			return new ProductionResult(i - 1);
		} else {
			Atom atom = new Atom(headAtom.getPred(), mgu.apply(headAtom.getArgs()));
			return new ProductionResult(atom);
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

	/**
	 * Applies the conjunction function to the list of facts and propagate with the
	 * clause certainty parameter.
	 * 
	 * @param rule              Rule to compute the head certainty for
	 * @param factInstantiation List of body facts to retrieve the certainty from
	 * @return Derived Certainty for the rule.
	 */
	protected Double computeHeadCertainty(Clause rule, List<Atom> factInstantiation) {
		// The conjunction function has greatest element if empty
		Double conjunction = ProblogEngineBase.GREATEST_ELEMENT;
		List<Double> bodyCertainties = factInstantiation.stream().map(this.factsRepo::getValuation).collect(toList());
		conjunction = ProblogEngineBase.conjunction(bodyCertainties);

		return ProblogEngineBase.propagation(conjunction, rule.getCertainty());
	}

	private static class ProductionResult {
		private final Atom atom;
		private final boolean unified;
		private final int failurePosition;

		public ProductionResult(Atom atom) {
			this.unified = true;
			this.failurePosition = -1;
			this.atom = atom;
		}

		public ProductionResult(int failurePosition) {
			this.unified = false;
			this.failurePosition = failurePosition;
			this.atom = null;
		}

		public Atom getAtom() {
			return atom;
		}

		public boolean isUnified() {
			return unified;
		}

		public int getFailurePosition() {
			return failurePosition;
		}
	}
}
