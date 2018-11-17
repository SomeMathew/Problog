/*******************************************************************************
 * This file is part of the AbcDatalog project.
 *
 * Copyright (c) 2016, Harvard University
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the BSD License which accompanies this distribution.
 *
 * The development of the AbcDatalog project has been supported by the 
 * National Science Foundation under Grant Nos. 1237235 and 1054172.
 *
 * See README for contributors.
 ******************************************************************************/
package edu.comp6591.problog.parser;

/**
 * An exception signifying a parsing error.
 *
 */
@SuppressWarnings("serial")
public class ProblogParseException extends Exception {
	/**
	 * Constructs an exception signifying a parsing error.
	 */
	public ProblogParseException() {
	}

	/**
	 * Constructs an exception signifying a parsing error.
	 * 
	 * @param message
	 *            the error message
	 */
	public ProblogParseException(String message) {
		super(message);
	}

	/**
	 * Constructs an exception signifying a parsing error.
	 * 
	 * @param cause
	 *            the exception that caused this exception
	 */
	public ProblogParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs an exception signifying a parsing error.
	 * 
	 * @param message
	 *            the error message
	 * @param cause
	 *            the exception that caused this exception
	 */
	public ProblogParseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an exception signifying a parsing error.
	 * 
	 * @param message
	 *            the error message
	 * @param cause
	 *            the exception that caused this exception
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 */
	public ProblogParseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
