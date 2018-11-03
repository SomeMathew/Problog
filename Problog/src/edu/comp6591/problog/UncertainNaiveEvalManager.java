package edu.comp6591.problog;

import java.util.Set;

import abcdatalog.ast.Clause;
import edu.comp6591.problog.validation.ProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import edu.comp6591.problog.validation.ProblogValidator;

public class UncertainNaiveEvalManager {
	private volatile boolean isInitialized = false;
	private volatile boolean isEvaluated = false;
	private volatile boolean isFinishing = false;

//	protected final List<ValidProblogClause> initialFacts;

	public void initialize(Set<Clause> program) throws ProblogValidationException {
		// TODO Auto-generated method stub
		if (this.isInitialized) {
			throw new IllegalStateException("Cannot initialize an evaluation manager more than once.");
		}
		ProblogProgram prog = (new ProblogValidator()).withUncertainty().validate(program);
//		initialFacts.addAll(prog.getInitialFacts());

		this.isInitialized = true;

	}

//	public IndexableFactCollection eval() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
