package edu.comp6591.problog.engine;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.datastructure.Statistics;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.util.Collection;

import java.util.Map;
import java.util.function.Function;

/**
 * Interface exposing methods for the different Problog engines
 */
public interface IProblogEngine {
	void setParameter(ProblogEngineFactory.Parameter param, Function<Collection<Double>, Double> function);
	
	void init(IProblogProgram program) throws ProblogValidationException;

	Map<Atom, Double> getComputedDatabase();

	Map<Atom, Double> query(Atom query);
	
	Statistics getStats();
}
