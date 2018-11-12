package edu.comp6591.problog.engine;

import edu.comp6591.problog.util.ParameterHelper;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Base class defining common methods and variables for all Problog engines
 */
public abstract class ProblogEngineBase implements IProblogEngine {

	public static final Function<List<Double>, Double> disjunctionParam = ParameterHelper::independence;
	public static final Function<List<Double>, Double> conjunctionParam = ParameterHelper::minimum;
	public static final Function<List<Double>, Double> propagationParam = ParameterHelper::product;

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
	 * @param uncertainties
	 * @return calculated uncertainty
	 */
	public static Double propagation(List<Double> uncertainties) {
		return propagationParam.apply(uncertainties);
	}
}
