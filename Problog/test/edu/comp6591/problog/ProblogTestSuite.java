package edu.comp6591.problog;

import edu.comp6591.problog.test.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Perform all unit tests for the Problog program
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	ProblogParserValidatorTest.class,
	ProblogNaiveEngineTest.class,
	ProblogSemiNaiveEngineTest.class
})
public class ProblogTestSuite {
}
