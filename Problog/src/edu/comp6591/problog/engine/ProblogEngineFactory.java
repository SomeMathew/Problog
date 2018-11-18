package edu.comp6591.problog.engine;

import edu.comp6591.problog.engine.naive.ProblogNaiveEngine;

/**
 * Problog engine factory
 */
public class ProblogEngineFactory {
	
	public enum Mode {
		Naive,
		SemiNaive
	}
	
	/**
	 * Create engine according to mode
	 * @param mode
	 * @return desired engine
	 * @throws ProblogEngineException 
	 */
	public static IProblogEngine createEngine(Mode mode) throws ProblogEngineException {
		switch (mode) {
			case Naive:
				return new ProblogNaiveEngine();
			case SemiNaive:
			default:
				throw new ProblogEngineException("The engine mode is not implemented");
		}
	}
	
	/**
	 * Parse engine mode from string option
	 * @param entry
	 * @return engine mode
	 * @throws ProblogEngineException 
	 */
	public static Mode parseMode(String entry) throws ProblogEngineException {
		Integer number = Integer.parseInt(entry);
		switch(number) {
			case 1:
				return Mode.Naive;
			case 2:
				return Mode.SemiNaive;
			default:
				throw new ProblogEngineException("The engine mode is not valid");
		}
	}
}
