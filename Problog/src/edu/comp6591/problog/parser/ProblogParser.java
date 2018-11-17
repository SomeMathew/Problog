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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abcdatalog.parser.DatalogTokenizer;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Constant;
import edu.comp6591.problog.ast.ITerm;
import edu.comp6591.problog.ast.Predicate;
import edu.comp6591.problog.ast.Variable;

/**
 * A recursive descent parser for Problog adapted from the abcDatalog parser.
 * <br>
 * <br>
 * A Problog program is a set of clauses, where each clause is in the form "a0
 * :- a1, ..., an : c." or "a0 : c." and each ai is an atom of the form "pi" or
 * "pi(t1, ..., tki)" for ki > 0 such that pi is a predicate symbol, each tj for
 * 0 < j <= ki is a term (i.e. a constant or variable) and each c is a double in
 * [0,1]. Any variable in a0 must appear in at least one of ai, ..., an.
 * Identifiers can contain letters, digits and underscores. Identifiers that
 * begin with an upper case letter or an underscore are parsed as variables.
 *
 * Credit to the abcDatalog project for this parser, it was adapted for the
 * problog language with the addition of the certainty for a rule or ground
 * fact.
 * 
 * The following modifications were done to the parse:
 * <ul>
 * <li>Removal of
 * <ul>
 * <li>negation
 * <li>unifier
 * <li>disunifier
 * <li>query parsing
 * </ul>
 * <li>Extension of the grammar for the certainty element
 * <li>Uses the problog AST which is a simplified version of the original
 * abcdatalog ast.
 */
public final class ProblogParser {

	/**
	 * Class cannot be instantiated.
	 */
	private ProblogParser() {
	}

	/**
	 * Generates an abstract syntax tree representation of the program described by
	 * the provided token stream.
	 * 
	 * @param t the token stream representation of program
	 * @return the AST of program
	 * @throws ProblogParseException
	 */
	public static List<Clause> parseProgram(DatalogTokenizer t) throws ProblogParseException {
		List<Clause> clauses = new LinkedList<>();
		while (t.hasNext()) {
			clauses.add(parseClause(t));
		}
		return clauses;
	}

	/**
	 * Attempts to extract a clause from the provided token stream.
	 * 
	 * @param t the token stream
	 * @return the clause
	 * @throws ProblogParseException
	 */
	private static Clause parseClause(DatalogTokenizer t) throws ProblogParseException {
		// Parse the head of the clause.
		Atom head = parseAtom(t);
		Double probability = null;

		// Parse the body (if any).
		List<Atom> body = new ArrayList<>();
		// We have a body or probability
		if (t.peek().equals(":")) {
			t.consume(":");
			// Body
			if (t.peek().equals("-")) {
				t.consume("-");
				while (true) {
					body.add(parseConjunct(t));
					if (t.peek().equals(":") || t.peek().equals(".")) {
						break;
					}
					t.consume(",");
				}
			}

			// We have a probability
			if (!t.peek().equals(".")) {
				if (t.peek().equals(":")) {
					t.consume(":");
				}
				probability = parseProbability(t);
			}
		}
		t.consume(".");

		return new Clause(head, body, probability == null ? 1 : probability);
	}

	private static Atom parseConjunct(DatalogTokenizer t) throws ProblogParseException {
		String id = parseIdentifier(t);
		return parseAtom(id, t);
	}

	private static Atom parseAtom(String predSym, DatalogTokenizer t) throws ProblogParseException {
		char first = predSym.charAt(0);
		if (Character.isUpperCase(first)) {
			throw new ProblogParseException(
					"Invalid predicate symbol \"" + predSym + "\" begins with an upper case letter.");
		}
		if (first == '_') {
			throw new ProblogParseException("Invalid predicate symbol \"" + predSym + "\" begins with an underscore.");
		}

		ArrayList<ITerm> args = new ArrayList<>();
		// If followed by a parenthesis, must have arity greater than zero.
		if (t.peek().equals("(")) {
			t.consume("(");
			while (true) {
				args.add(parseTerm(t.next()));
				if (t.peek().equals(")")) {
					// End of the arguments!
					break;
				}
				t.consume(",");
			}
			t.consume(")");
		}
		args.trimToSize();
		return new Atom(new Predicate(predSym, args.size()), args);
	}

	/**
	 * Attempts to extract an atom from the provided token stream.
	 * 
	 * @param t    the token stream
	 * @param vars records the variables in this atom
	 * @return the atom
	 * @throws ProblogParseException
	 */
	private static Atom parseAtom(DatalogTokenizer t) throws ProblogParseException {
		return parseAtom(parseIdentifier(t), t);
	}

	private static ITerm parseTerm(String s) throws ProblogParseException {
		if (s.equals("_")) {
			return new Variable("_");
		}
		char c = s.charAt(0);
		if (Character.isUpperCase(c) || c == '_') {
			return new Variable(s);
		}
		return new Constant(s);
	}

	/**
	 * Attempts to extract a valid identifier from the supplied token stream. Valid
	 * identifiers are formed from alphanumeric characters and underscores.
	 * 
	 * @param t the token stream
	 * @return the identifier
	 * @throws ProblogParseException
	 */
	private static String parseIdentifier(DatalogTokenizer t) throws ProblogParseException {
		String s = t.next();
		// Check to make sure it contains only appropriate characters for an
		// identifier.
		boolean okay = true;
		for (int i = 0; i < s.length(); ++i) {
			okay &= Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '_';
		}
		if (!okay) {
			throw new ProblogParseException("Invalid identifier \"" + s + "\" not alphanumeric.");
		}
		return s;
	}

	/**
	 * Attempts to extract an atom representation of the query described in the
	 * token stream.
	 * 
	 * @param t the token stream
	 * @return the atom
	 * @throws DatalogParseException
	 */
	public static Atom parseQuery(DatalogTokenizer t) throws ProblogParseException {
		Atom r = parseAtom(t);
		t.consume("?");
		return r;
	}

//	/**
//	 * Extracts an atom from the token stream. The atom must be followed by a
//	 * period.
//	 * 
//	 * @param t the token stream
//	 * @return the atom
//	 * @throws DatalogParseException
//	 */
//	public static Atom parseClauseAsPositiveAtom(DatalogTokenizer t) throws ProblogParseException {
//		PositiveAtom r = parseAtom(t);
//		t.consume(".");
//		return r;
//	}

	/**
	 * Extract the probability from the token stream.
	 * 
	 * Probability must be between 0 and 1 and well formed (e.g. 0.1 not .1)
	 * 
	 * This is necessary since the tokenizer is set to tokenize the "." which
	 * prevents parsing floats.
	 * 
	 * @param t the token stream
	 * @return the probability as a double
	 * @throws ProblogParseException
	 */
	public static double parseProbability(DatalogTokenizer t) throws ProblogParseException {
		StringBuilder sb = new StringBuilder();
		String tok = t.next();
		sb.append(tok);
		t.consume(".");
		if (t.peek().matches("\\d+")) {
			sb.append(".");
			do {
				sb.append(t.next());
			} while (!t.peek().equals("."));
		}
		return Double.parseDouble(sb.toString());
	}
}
