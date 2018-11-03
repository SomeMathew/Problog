package edu.comp6591.problog.util;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.parser.DatalogParseException;
import abcdatalog.parser.DatalogParser;
import abcdatalog.parser.DatalogTokenizer;
import edu.comp6591.problog.validation.ProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import edu.comp6591.problog.validation.ProblogValidator;
import java.io.StringReader;
import java.util.Set;

/**
 * AST model conversion methods
 */
public class ASTHelper {
	
	/**
	 * Convert data string to AST database
	 * @param data
	 * @return ProblogProgram
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	public static ProblogProgram getProgram(String data) throws DatalogParseException, ProblogValidationException {
		StringReader sr = new StringReader(data);
		DatalogTokenizer dt = new DatalogTokenizer(sr);
		
		Set<Clause> ast = DatalogParser.parseProgram(dt);
		return new ProblogValidator().withUncertainty().validate(ast);
    }
	
	/**
	 * Convert query string to AST model
	 * @param query
	 * @return PositiveAtom
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	public static PositiveAtom getGoal(String query) throws DatalogParseException, ProblogValidationException {
		StringReader sr = new StringReader(query);
		DatalogTokenizer dt = new DatalogTokenizer(sr);
		
		return DatalogParser.parseQuery(dt);
    }
}
