package edu.comp6591.problog;

import java.util.List;
import java.util.Set;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.validation.DatalogValidationException;
import abcdatalog.engine.bottomup.EvalManager;
import abcdatalog.util.Utilities;
import abcdatalog.util.datastructures.IndexableFactCollection;
import edu.comp6591.problog.validator.ProblogProgram;
import edu.comp6591.problog.validator.ProblogValidator;
import edu.comp6591.problog.validator.ProblogValidator.ValidProblogClause;

public class UncertainNaiveEvalManager implements EvalManager {
	private volatile boolean isInitialized = false;
	private volatile boolean isEvaluated = false;
	private volatile boolean isFinishing = false;
	
	protected final List<ValidProblogClause> initialFacts;

	@Override
	public void initialize(Set<Clause> program) throws DatalogValidationException {
		// TODO Auto-generated method stub
		if (this.isInitialized) {
			throw new IllegalStateException("Cannot initialize an evaluation manager more than once.");
		}
		ProblogProgram prog = (new ProblogValidator()).withUncertainty().validate(program);
		initialFacts.addAll(prog.getInitialFacts());

		this.isInitialized = true;

	}

	@Override
	public IndexableFactCollection eval() {
		// TODO Auto-generated method stub
		return null;
	}

}
