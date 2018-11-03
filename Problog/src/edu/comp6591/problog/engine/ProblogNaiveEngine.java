package edu.comp6591.problog.engine;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.util.Set;

/**
 * Naive algorithm to process the Problog database
 */
public class ProblogNaiveEngine implements IProblogEngine {
	
	/**
	 * Initialize the engine with a set of clauses and calculate fixpoint
	 * @param clauses
	 * @throws ProblogValidationException 
	 */
	@Override
	public void init(Set<Clause> clauses) throws ProblogValidationException {
		throw new UnsupportedOperationException("ProblogNaiveEngine 'init' method not supported yet.");
	}
	
	/**
	 * Browse the engine's database and return the result(s) of the given query
	 * @param query
	 * @return result(s)
	 */
	@Override
	public Set<PositiveAtom> query(PositiveAtom query) {
		throw new UnsupportedOperationException("ProblogNaiveEngine 'query' method not supported yet.");
	}
}
