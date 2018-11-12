package edu.comp6591.problog.util;

import java.util.List;
import java.util.Set;

/**
 * Provide methods to use as parameter function
 */
public class ParameterHelper {

	/**
	 * Calculate union of probabilities of list of independent clauses
	 * 
	 * @param list
	 * @return final uncertainty
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
	 * @return final uncertainty
	 */
	public static Double minimum(List<Double> list) {
		double result = 0;
		for (Double probability : list) {
			result = result < probability ? result : probability;
		}
		return result;
	}

	/**
	 * Calculate product of probabilities of list of clauses
	 * 
	 * @param list
	 * @return final uncertainty
	 */
	public static Double product(List<Double> list) {
		double result = 0;
		double identity = 1; // Used avoid returning 1 for empty list
		for (Double probability : list) {
			identity *= probability;
			result = identity;
		}
		return result;
	}
}
