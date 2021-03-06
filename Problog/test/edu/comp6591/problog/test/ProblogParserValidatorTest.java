package edu.comp6591.problog.test;

import abcdatalog.parser.DatalogTokenizer;
import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.util.ASTHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.util.Set;
import org.junit.Test;

/**
 * Testing parsing and validation of various types of input sets
 */
public class ProblogParserValidatorTest {

	private String program;
	private String query;
	private IProblogProgram validProgram;
	private DatalogTokenizer t;
	private Set<Clause> ast;
	private Atom goal;

	/**
	 * Check parsing of probabilities
	 * 
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	@Test
	public void ProbabilitiesTest() throws ProblogParseException, ProblogValidationException {
		program = "edge(0,1) : 0.5. edge(1,2) : 0.5. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle(X) :- tc(X,X) : 0.3.";
		validProgram = ASTHelper.getProgram(program);
	}

	/**
	 * Check multiset for rules with different probability for same head
	 * 
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	@Test
	public void DifferentHeadProbabilitiesTest() throws ProblogParseException, ProblogValidationException {
		program = "edge(0,1) : 0.5. edge(1,2) : 0.5. edge(0,1) : 0.2. edge(0,1) : 0.1. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle(X) :- tc(X,X) : 0.3.";
		validProgram = ASTHelper.getProgram(program);
	}

	/**
	 * Check validation of probability constraint
	 * 
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	@Test(expected = ProblogParseException.class)
	public void ProbabilityConstraintTest() throws ProblogParseException, ProblogValidationException {
		program = "edge(0,1) : 0.5. edge(1,2) : 1.25. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle(X) :- tc(X,X) : 1.3.";
		validProgram = ASTHelper.getProgram(program);
	}

	/**
	 * Check normal datalog program
	 * 
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	@Test
	public void NormalProgramTest() throws ProblogParseException, ProblogValidationException {
		program = "edge(0,1). edge(1,2). tc(X,Y) :- edge(X,Y)." + "tc(X,Y) :- edge(X,Z), tc(Z,Y). cycle(X) :- tc(X,X).";
		validProgram = ASTHelper.getProgram(program);
	}

	/**
	 * Check negated atom not allowed
	 * 
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	@Test(expected = Exception.class)
	public void DisallowNegatedAtomTest() throws ProblogParseException, ProblogValidationException {
		program = "lk_1(a,b). lk_1(b,c). reachable(X,Y) :- lk_1(X,Y)."
				+ "reachable(X,Y) :- lk_1(X,Z), not reachable(Z,Y).";
		validProgram = ASTHelper.getProgram(program);
	}

	/**
	 * Check goals
	 * 
	 * @throws ProblogParseException
	 * @throws ProblogValidationException
	 */
	@Test
	public void GoalsTest() throws ProblogParseException, ProblogValidationException {
		query = "lk_1(a,X)?";
		goal = ASTHelper.getGoal(query);
	}
}
