package edu.comp6591.problog.test;

import edu.comp6591.problog.datastructure.Statistics;
import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineException;
import edu.comp6591.problog.engine.ProblogEngineFactory;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.util.ASTHelper;
import edu.comp6591.problog.util.FileHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.validation.ProblogValidationException;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class ProblogNaiveEngineBenchmark {
	    
	private String program;
	private IProblogProgram validProgram;
	private IProblogEngine engine;
	private Statistics stats;
	private int threshold = 500;

	@Test
	public void BenchmarkTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException, IOException {
		
		File folder = new File(".\\Benchmark\\input");
		File[] list = folder.listFiles();
		
		for (File benchmark : list) {
			program = FileHelper.getFile(benchmark.getPath());
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.Naive, validProgram);
			engine.init(validProgram);
			System.out.println(benchmark.getName() +"\n" + engine.getStats());
		}
	}
}
