package edu.comp6591.problog.util;

import abcdatalog.parser.DatalogTokenizer;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.datastructure.Statistics;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.parser.ProblogParser;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogProgramCreator;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

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
	 * Convert collection of atoms with uncertainty to readable string
	 * 
	 * @param facts
	 * @return facts as string
	 */
	public static String printFacts(Map<Atom, Double> facts) {
		if (facts == null) {
			return null;
		}
		
		StringBuilder formatted = new StringBuilder();
		
		for (Map.Entry<Atom, Double> fact : facts.entrySet()) {
			formatted.append(fact.getKey().toString())
				.append(" : ")
				.append(fact.getValue())
				.append(".\n");
		}
		
		return formatted.toString();
	}
	
	/**
	 * Convert statistics to readable string
	 * 
	 * @param stats
	 * @return stats as string
	 */
	public static String printStatistics(Statistics stats) {
		if (stats == null) {
			return null;
		}
		
		StringBuilder formatted = new StringBuilder();
		formatted.append("STATISTICS\nExecution time (ms): ")
			.append(stats.DurationMS)
			.append("\nNumber of iterations: ")
			.append(stats.Iterations)
			.append("\nSize of EDB: ")
			.append(stats.EDBSize)
			.append("\nSize of IDB: ")
			.append(stats.IDBSize)
			.append("\n");
		
		return formatted.toString();
	}
}
