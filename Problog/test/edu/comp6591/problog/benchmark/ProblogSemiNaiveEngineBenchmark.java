package edu.comp6591.problog.benchmark;

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

public class ProblogSemiNaiveEngineBenchmark {
	
	private String program;
	private IProblogProgram validProgram;
	private IProblogEngine engine;
	private final int threshold = 500;
	
	@Test
	public void BenchmarkTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException, IOException {
		
		File folder = new File(".\\Benchmark\\input");
		File[] list = folder.listFiles();
		
		// Warmup
		for (int i = 0; i < threshold; i++) {
			program = FileHelper.getFile(folder + "\\clique10_0.5.pl");
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.init(validProgram);
		}
		
		for (File benchmark : list) {
			program = FileHelper.getFile(benchmark.getPath());
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.init(validProgram);
			System.out.println(benchmark.getName() +"\n" + engine.getStats());
			FileHelper.setFile(".\\Benchmark\\output\\" + benchmark.getName().replace(".pl", "_seminaive.pl"), 
				ASTHelper.printFacts(engine.getComputedDatabase()));
		}
	}
}
