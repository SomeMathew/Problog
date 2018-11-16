package edu.comp6591.problog.engine;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.datastructure.AtomKey;
import edu.comp6591.problog.validation.ProblogProgramOLD;
import edu.comp6591.problog.validation.ProblogValidationException;

import java.util.Map;
import java.util.Set;

/**
 * Interface exposing methods for the different Problog engines
 */
public interface IProblogEngine {
	void init(ProblogProgramOLD program) throws ProblogValidationException;
	Map<AtomKey, Double> getComputedDatabase();
	Set<Atom> query(Atom query);
}
