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
	 * @param param
	 * @param entry
	 * @return engine parameter function
	 * @throws ProblogEngineException
	 */
	public static Function<Collection<Double>, Double> parseParam(ProblogEngineFactory.Parameter param, String entry) throws ProblogEngineException {
		Integer number = Integer.parseInt(entry);
		switch (param) {
			case Disjunction:
				switch (number) {
					case 1:
						return ParameterHelper::independence;
					case 2:
						return ParameterHelper::maximum;
					default:
						throw new ProblogEngineException("The engine parameter is not valid");
				}
			case Conjunction:
			case Propagation:
				switch (number) {
					case 1:
						return ParameterHelper::product;
					case 2:
						return ParameterHelper::minimum;
					default:
						throw new ProblogEngineException("The engine parameter is not valid");
				}
			default:
				throw new ProblogEngineException("The engine parameter is not valid");
		}
	}
}
