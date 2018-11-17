package edu.comp6591.problog.util;

import abcdatalog.parser.DatalogTokenizer;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.parser.ProblogParser;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogProgramCreator;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

/**
 * AST model conversion methods
 */
public class ASTHelper {

	/**
	 * Convert data string to AST model
	 * 
	 * @param data
	 * @return ProblogProgram
	 * @throws ProblogParseException
	 */
	public static List<Clause> getClauses(String data) throws ProblogParseException {
		StringReader sr = new StringReader(data);
		DatalogTokenizer dt = new DatalogTokenizer(sr);
		return ProblogParser.parseProgram(dt);
	}

	/**
	 * Convert data string to AST database
	 * 
	 * @param data
	 * @return ProblogProgram
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	public static IProblogProgram getProgram(String data) throws ProblogParseException, ProblogValidationException {
		List<Clause> ast = getClauses(data);
		return ProblogProgramCreator.create(ast);
	}

	/**
	 * Convert query string to AST model
	 * 
	 * @param query
	 * @return PositiveAtom
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	public static Atom getGoal(String query) throws ProblogParseException, ProblogValidationException {
		StringReader sr = new StringReader(query);
		DatalogTokenizer dt = new DatalogTokenizer(sr);

		return ProblogParser.parseQuery(dt);
	}

	/**
	 * Convert AST result(s) to string (either list of atoms or T/F)
	 * 
	 * @param goal
	 * @param results
	 * @return result(s) string
	 */
	public static String setResults(Atom goal, Set<Atom> results) {
		throw new UnsupportedOperationException("ASTHelper 'setResults' method not supported yet.");
	}
}
