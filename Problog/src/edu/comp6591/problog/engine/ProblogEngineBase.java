package edu.comp6591.problog.engine;

import edu.comp6591.problog.ast.ITerm;
import edu.comp6591.problog.ast.TermVisitor;
import edu.comp6591.problog.util.ParameterHelper;

import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import abcdatalog.util.substitution.UnionFindBasedUnifier;

/**
 * Base class defining common methods and variables for all Problog engines
 */
public abstract class ProblogEngineBase implements IProblogEngine {
	public static final double LEAST_ELEMENT = 0;
	public static final double GREATEST_ELEMENT = 1;

	public static final Function<List<Double>, Double> disjunctionParam = ParameterHelper::independence;
	public static final Function<List<Double>, Double> conjunctionParam = ParameterHelper::minimum;
	public static final BinaryOperator<Double> propagationParam = ParameterHelper::product;

	/**
	 * Apply and execute independence function for disjunction parameter
	 * 
	 * @param uncertainties
	 * @return calculated uncertainty
	 */
	public static Double disjunction(List<Double> uncertainties) {
		return disjunctionParam.apply(uncertainties);
	}

	/**
	 * Apply and execute minimum function for conjunction parameter
	 * 
	 * @param uncertainties
	 * @return calculated uncertainty
	 */
	public static Double conjunction(List<Double> uncertainties) {
		return conjunctionParam.apply(uncertainties);
	}

	/**
	 * Apply and execute product function for propagation parameter
	 * 
	 * @param bodyCertainty
	 * @param ruleCertainty
	 * @return calculated certainty
	 */
	public static Double propagation(Double bodyCertainty, Double ruleCertainty) {
		return propagationParam.apply(bodyCertainty, ruleCertainty);
	}

	/**
	 * Try to unify the array of terms, this will update the Unifier.
	 * 
	 * @param mgu
	 * @param ruleTerms
	 * @param factTerms
	 * @return True if it could successfully unify the terms.
	 */
	protected boolean unifyArgs(UnionFindBasedUnifier mgu, List<ITerm> ruleTerms, List<ITerm> factTerms) {
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
}
