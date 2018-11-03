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
package edu.comp6591.problog.validation;

@SuppressWarnings("serial")
public class ProblogValidationException extends Exception {

	public ProblogValidationException() {}

	public ProblogValidationException(String message) {
		super(message);
	}

	public ProblogValidationException(Throwable cause) {
		super(cause);
	}

	public ProblogValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProblogValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
