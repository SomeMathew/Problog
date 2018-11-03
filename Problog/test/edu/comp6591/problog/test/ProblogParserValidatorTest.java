package edu.comp6591.problog.test;

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
import org.junit.Test;

/**
 * Testing parsing and validation of various types of input sets
 */
public class ProblogParserValidatorTest {
	
	String program;
	ProblogProgram validProgram;
	DatalogTokenizer t;
	Set<Clause> ast;
	PositiveAtom goal;
	
	public ProblogParserValidatorTest() {
	}
	
	/**
	 * Check parsing of probabilities
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	@Test
	public void ProbabilitiesTest() throws DatalogParseException, ProblogValidationException {
		program = "edge(0,1) : 0.5. edge(1,2) : 0.5. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle :- tc(X,X) : 0.3.";
		
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		
		validProgram = new ProblogValidator().withUncertainty().validate(ast);
	}
	
	/**
	 * Check multiset for rules with different probability for same head
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	@Test
	public void DifferentHeadProbabilitiesTest() throws DatalogParseException, ProblogValidationException {
		program = "edge(0,1) : 0.5. edge(1,2) : 0.5. edge(0,1) : 0.2. edge(0,1) : 0.1. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle :- tc(X,X) : 0.3.";
		
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);

		validProgram = new ProblogValidator().withUncertainty().validate(ast);
	}
	
	/**
	 * Check validation of probability constraint
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	@Test(expected = ProblogValidationException.class)
	public void ProbabilityConstraintTest() throws DatalogParseException, ProblogValidationException {
		program = "edge(0,1) : 0.5. edge(1,2) : 1.25. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle :- tc(X,X) : 0.3.";
		
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		
		validProgram = new ProblogValidator().withUncertainty().validate(ast);
	}
	
	/**
	 * Check normal datalog program
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	@Test
	public void NormalProgramTest() throws DatalogParseException, ProblogValidationException {
		program = "edge(0,1). edge(1,2). tc(X,Y) :- edge(X,Y)." + "tc(X,Y) :- edge(X,Z), tc(Z,Y). cycle :- tc(X,X).";
		
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		
		validProgram = new ProblogValidator().withUncertainty().validate(ast);
	}
	
	/**
	 * Check negated atom not allowed
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	@Test(expected = ProblogValidationException.class)
	public void DisallowNegatedAtomTest() throws DatalogParseException, ProblogValidationException {
		program = "lk_1(a,b). lk_1(b,c). reachable(X,Y) :- lk_1(X,Y)."
				+ "reachable(X,Y) :- lk_1(X,Z), %ignore\n not reachable(Z,Y).";
		
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		
		validProgram = new ProblogValidator().withUncertainty().validate(ast);
	}
	
	/**
	 * Check goals
	 * @throws DatalogParseException
	 * @throws ProblogValidationException 
	 */
	@Test
	public void GoalsTest() throws DatalogParseException, ProblogValidationException {
		String query = "lk_1(a,X)?";
		
		t = new DatalogTokenizer(new StringReader(query));
		goal = DatalogParser.parseQuery(t);
	}
}
