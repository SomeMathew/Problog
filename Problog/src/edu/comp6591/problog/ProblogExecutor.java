package edu.comp6591.problog;

import java.util.Set;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.validation.DatalogValidationException;
import abcdatalog.engine.bottomup.EvalManager;
import abcdatalog.executor.DatalogListener;

public class ProblogExecutor {
	/**
	 * Whether the runner thread has been started. This is guarded by this
	 * executor's inherent lock.
	 */
	private volatile boolean isInitialized = false, isRunning = false;

	private volatile EvalManager evalManager;

	public synchronized void initialize(Set<Clause> program, EvalManager evalManager)
			throws DatalogValidationException {
		if (this.isRunning) {
			throw new IllegalStateException("Cannot initialize an executor that is already running).");
		}
		if (this.isInitialized) {
			throw new IllegalStateException("Executor already initialized.");
		}

		this.evalManager = evalManager;
		this.evalManager.initialize(program);
		this.isInitialized = true;
	}

	public synchronized void start() {
		if (this.isRunning) {
			throw new IllegalStateException("Executor is already running.");
		}
		if (!this.isInitialized) {
			throw new IllegalStateException("Executor has not been initialized.");
		}
		this.evalManager.eval();
		this.isRunning = true;
	}

	public void registerListener(PredicateSym p, DatalogListener listener) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("STUB: registerListner is not currently supported.");
	}
}
