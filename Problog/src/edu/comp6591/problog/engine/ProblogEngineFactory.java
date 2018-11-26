package edu.comp6591.problog.engine;

import edu.comp6591.problog.engine.naive.ProblogNaiveEngine;
import edu.comp6591.problog.engine.seminaive.ProblogSemiNaiveEngine;
import edu.comp6591.problog.util.ParameterHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import java.util.Collection;
import java.util.function.Function;

/**
 * Problog engine factory
 */
public class ProblogEngineFactory {

	public enum Mode {
		Naive, SemiNaive
	}

	public enum Parameter {
		Disjunction,
		Conjunction,
		Propagation
	}

	/**
	 * Create engine according to mode
	 * 
	 * @param mode
	 * @param program
	 * @return desired engine
	 * @throws ProblogEngineException
	 */
	public static IProblogEngine createEngine(Mode mode, IProblogProgram program) throws ProblogEngineException {
		switch (mode) {
		case Naive:
			return new ProblogNaiveEngine(program);
		case SemiNaive:
			return new ProblogSemiNaiveEngine(program);
		default:
			throw new ProblogEngineException("The engine mode is not implemented");
		}
	}

	/**
	 * Parse engine mode from string option
	 * 
	 * @param entry
	 * @return engine mode
	 * @throws ProblogEngineException
	 */
	public static Mode parseMode(String entry) throws ProblogEngineException {
		Integer number = Integer.parseInt(entry);
		switch (number) {
		case 1:
			return Mode.Naive;
		case 2:
			return Mode.SemiNaive;
		default:
			throw new ProblogEngineException("The engine mode is not valid");
		}
	}

	/**
	 * Parse engine parameter function from string option
	 * 
	 * @param entry
	 * @return engine parameter function
	 * @throws ProblogEngineException
	 */
	public static Function<Collection<Double>, Double> parseParam(String entry) throws ProblogEngineException {
		Integer number = Integer.parseInt(entry);
		switch (number) {
		case 0:
			return null;
		case 1:
			return ParameterHelper::independence;
		case 2:
			return ParameterHelper::minimum;
		case 3:
			return ParameterHelper::maximum;
		case 4:
			return ParameterHelper::product;
		case 5:
			return ParameterHelper::sum;
		case 6:
			return ParameterHelper::average;
		case 7:
			return ParameterHelper::median;
		default:
			throw new ProblogEngineException("The engine mode is not valid");
		}
	}
}
