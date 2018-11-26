package edu.comp6591.problog.util;

import java.util.Collection;
import java.util.Collections;

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
	public static Double independence(Collection<Double> list) {
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
	public static Double minimum(Collection<Double> list) {
		return Collections.min(list);
	}

	/**
	 * Calculate maximum probability of list of clauses
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double maximum(Collection<Double> list) {
		return Collections.max(list);
	}

	/**
	 * Calculate product of probabilities.
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double product(Collection<Double> list) {
		if (list == null || list.isEmpty()) {
			return new Double(0);
		}
		
		double result = 1;
		for (Double probability : list) {
			result *= probability;
		}
		return result;
	}

	/**
	 * Calculate sum of probabilities.
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double sum(Collection<Double> list) {
		double result = 1;
		for (Double probability : list) {
			result += probability;
		}
		return result;
	}

	/**
	 * Calculate average of probabilities.
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double average(Collection<Double> list) {
		if (list == null || list.isEmpty()) {
			return new Double(0);
		}
		return sum(list) / list.size();
	}

	/**
	 * Calculate median of probabilities.
	 * 
	 * @param list
	 * @return final certainty
	 */
	public static Double median(Collection<Double> list) {
		double min = minimum(list);
		double max = maximum(list);
		return (max - min) / 2;
	}
}
