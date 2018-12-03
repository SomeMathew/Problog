package edu.comp6591.problog.test.system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.base.Strings;
import com.google.common.collect.Streams;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineException;
import edu.comp6591.problog.engine.ProblogEngineFactory;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.test.TestCandidates;
import edu.comp6591.problog.util.ASTHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;

class SystemTestNaiveEqualsSemiNaive {
    private Logger LOGGER = Logger.getLogger(SystemTestNaiveEqualsSemiNaive.class.getName());

    private IProblogProgram validProgramNaive;
    private IProblogEngine engineNaive;

    private IProblogProgram validProgramSemiNaive;
    private IProblogEngine engineSemiNaive;

    @ParameterizedTest
    @MethodSource("programProvider")
    void testIterationCountIsEqual(String program)
            throws ProblogParseException, ProblogValidationException, ProblogEngineException {
        validProgramNaive = ASTHelper.getProgram(program);
        engineNaive = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive, validProgramNaive);
        engineNaive.init(validProgramNaive);
        LOGGER.info("Multipath rules:\n" + engineNaive.getComputedDatabase().toString() + "\n"
                + engineNaive.getStats().toString());

        validProgramSemiNaive = ASTHelper.getProgram(program);
        engineSemiNaive = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgramSemiNaive);
        engineSemiNaive.init(validProgramSemiNaive);
        LOGGER.info("Multipath rules:\n" + engineSemiNaive.getComputedDatabase().toString() + "\n"
                + engineSemiNaive.getStats().toString());

        assertEquals(engineNaive.getStats().Iterations, engineSemiNaive.getStats().Iterations);
    }

    @ParameterizedTest
    @MethodSource("programProvider")
    void testIDBSizeIsEqual(String program)
            throws ProblogParseException, ProblogValidationException, ProblogEngineException {
        validProgramNaive = ASTHelper.getProgram(program);
        engineNaive = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive, validProgramNaive);
        engineNaive.init(validProgramNaive);
        LOGGER.info("Multipath rules:\n" + engineNaive.getComputedDatabase().toString() + "\n"
                + engineNaive.getStats().toString());

        validProgramSemiNaive = ASTHelper.getProgram(program);
        engineSemiNaive = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgramSemiNaive);
        engineSemiNaive.init(validProgramSemiNaive);
        LOGGER.info("Multipath rules:\n" + engineSemiNaive.getComputedDatabase().toString() + "\n"
                + engineSemiNaive.getStats().toString());

        assertEquals(engineNaive.getStats().IDBSize, engineSemiNaive.getStats().IDBSize);
    }

    @ParameterizedTest
    @MethodSource("programProvider")
    void testIDBAndEDBAreEqual(String program)
            throws ProblogParseException, ProblogValidationException, ProblogEngineException {
        validProgramNaive = ASTHelper.getProgram(program);
        engineNaive = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive, validProgramNaive);
        engineNaive.init(validProgramNaive);
        LOGGER.info("Naive Result:\n" + engineNaive.getComputedDatabase().toString() + "\n"
                + engineNaive.getStats().toString());

        validProgramSemiNaive = ASTHelper.getProgram(program);
        engineSemiNaive = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgramSemiNaive);
        engineSemiNaive.init(validProgramSemiNaive);
        LOGGER.info("Semi-Naive Result:\n" + engineSemiNaive.getComputedDatabase().toString() + "\n"
                + engineSemiNaive.getStats().toString());

        Map<Atom, Double> naiveResult = engineNaive.getComputedDatabase();
        Map<Atom, Double> semiNaiveResult = engineSemiNaive.getComputedDatabase();

        // Not a proper test unit test but only way to test equality with double in this
        // test.
        assertEquals(naiveResult.size(), semiNaiveResult.size());

        for (Entry<Atom, Double> entry : naiveResult.entrySet()) {
            Double semiNaiveValue = semiNaiveResult.get(entry.getKey());
            assertEquals(entry.getValue(), semiNaiveValue, 0.0000001, Strings.lenientFormat(
                    "Atom: atom %s || Naive: %s, Semi-Naive: %s", entry.getKey(), entry.getValue(), semiNaiveValue));
        }
    }

    static Stream<String> programProvider() throws IOException {
        return Arrays.stream(TestCandidates.allCandidates);
    }

}
