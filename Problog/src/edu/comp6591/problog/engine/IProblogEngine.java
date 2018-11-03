package edu.comp6591.problog.engine;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.util.Set;

/**
 * Interface exposing methods for the different Problog engines
 */
public interface IProblogEngine {
	void init(Set<Clause> program) throws ProblogValidationException;
	Set<PositiveAtom> query(PositiveAtom query);
}
