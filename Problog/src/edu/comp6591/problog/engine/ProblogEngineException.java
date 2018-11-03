package edu.comp6591.problog.engine;

/**
 * ProblogEngine exception
 */
@SuppressWarnings("serial")
public class ProblogEngineException extends Exception {

	public ProblogEngineException() {}

	public ProblogEngineException(String message) {
		super(message);
	}

	public ProblogEngineException(Throwable cause) {
		super(cause);
	}

	public ProblogEngineException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProblogEngineException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}