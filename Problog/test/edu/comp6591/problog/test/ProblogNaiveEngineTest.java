package edu.comp6591.problog.test;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineException;
import edu.comp6591.problog.engine.ProblogEngineFactory;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.util.ASTHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.util.Set;
import org.junit.Test;

/**
 * Testing fixpoint clause evaluator of naive Problog enigne
 */
public class ProblogNaiveEngineTest {
    
	private String program;
	private String query;
	private IProblogProgram validProgram;
        private IProblogEngine engine;
	private Atom goal;
	private Set<Atom> results;
        
	@Test
	public void NoUncertaintyCalculationTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException {
		program = "parent(mary,anna) : 1.0."
                        + "parent(john,anna) : 1.0."
                        + "parent(anna,daniel) : 1.0."
                        + "ancestor(X,Y) :- parent(X,Y): 1.0."
                        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 1.0.";
		validProgram = ASTHelper.getProgram(program);
                engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive);
                engine.init(validProgram);
                System.out.println("No uncertainty:\n" + engine.getComputedDatabase().toString());
//		query = "ancestor(mary,daniel)?";
//		goal = ASTHelper.getGoal(query);
//              results = engine.query(goal);
//              System.out.println(results.toString());
//              TODO: assert
	}
        
	@Test
	public void SimpleFactsUncertaintyCalculationTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException {
		program = "parent(mary,anna) : 0.5."
                        + "parent(john,anna) : 0.5."
                        + "parent(anna,daniel) : 0.8."
                        + "ancestor(X,Y) :- parent(X,Y): 1.0."
                        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 1.0.";
		validProgram = ASTHelper.getProgram(program);
                engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive);
                engine.init(validProgram);
                System.out.println("Simple facts uncertainty:\n" + engine.getComputedDatabase().toString());
//		query = "ancestor(mary,daniel)?";
//		goal = ASTHelper.getGoal(query);
//              results = engine.query(goal);
//              System.out.println(results.toString());
//              TODO: assert
	}
        
	@Test
	public void SimpleRulesUncertaintyCalculationTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException {
		program = "parent(mary,anna) : 1.0."
                        + "parent(john,anna) : 1.0."
                        + "parent(anna,daniel) : 1.0."
                        + "ancestor(X,Y) :- parent(X,Y): 0.8."
                        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
		validProgram = ASTHelper.getProgram(program);
                engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive);
                engine.init(validProgram);
                System.out.println("Simple rules uncertainty:\n" + engine.getComputedDatabase().toString());
//		query = "ancestor(mary,daniel)?";
//		goal = ASTHelper.getGoal(query);
//              results = engine.query(goal);
//              System.out.println(results.toString());
//              TODO: assert
	}
        
	@Test
	public void SimpleUncertaintyCalculationTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException {
		program = "parent(mary,anna) : 0.5."
                        + "parent(john,anna) : 0.5."
                        + "parent(anna,daniel) : 0.8."
                        + "ancestor(X,Y) :- parent(X,Y): 0.8."
                        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
		validProgram = ASTHelper.getProgram(program);
                engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive);
                engine.init(validProgram);
                System.out.println("Simple uncertainty:\n" + engine.getComputedDatabase().toString());
//		query = "ancestor(mary,daniel)?";
//		goal = ASTHelper.getGoal(query);
//              results = engine.query(goal);
//              System.out.println(results.toString());
//              TODO: assert
	}
}
