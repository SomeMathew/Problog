package edu.comp6591.problog.util;

import java.util.Collections;
import java.util.List;

/**
 * Provide methods to use as parameter function
 */
public class ParameterHelper {

	/**
	 * Calculate union of probabilities of list of independent clauses
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double independence(List<Double> list) {
		double result = 0;
		for (Double probability : list) {
			result += probability - (result * probability);
		}
		return result;
	}

	/**
	 * Calculate minimum probability of list of clauses
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double minimum(List<Double> list) {
		return Collections.min(list);
	}

	/**
	 * Calculate product of probabilities.
	 * 
	 * This is a propagation function.
	 * 
	 * @param d1
	 * @param d2
	 * @return final certainty
	 */
	public static Double product(Double d1, Double d2) {
		Double result = d1 * d2;
		return result;
	}
}
