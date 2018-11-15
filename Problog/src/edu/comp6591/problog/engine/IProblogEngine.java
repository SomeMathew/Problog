package edu.comp6591.problog.engine;

import abcdatalog.ast.PositiveAtom;
import edu.comp6591.problog.datastructure.AtomKey;
import edu.comp6591.problog.validation.ProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;

import java.util.Map;
import java.util.Set;

/**
 * Interface exposing methods for the different Problog engines
 */
public interface IProblogEngine {
	void init(ProblogProgram program) throws ProblogValidationException;
	Map<AtomKey, Double> getComputedDatabase();
	Set<PositiveAtom> query(PositiveAtom query);
}
