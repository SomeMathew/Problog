package edu.comp6591.problog.benchmark;

import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineException;
import edu.comp6591.problog.engine.ProblogEngineFactory;
import edu.comp6591.problog.parser.ProblogParseException;
import edu.comp6591.problog.util.ASTHelper;
import edu.comp6591.problog.util.FileHelper;
import edu.comp6591.problog.util.ParameterHelper;
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
			program = FileHelper.getFile(".\\TestPrograms\\easyLoop.problog");
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.init(validProgram);
		}
		
		for (File benchmark : list) {
			program = FileHelper.getFile(benchmark.getPath());
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.setThreshold(0.001);
			engine.init(validProgram);
			System.out.println("Default - " + benchmark.getName() +"\n" + engine.getStats());
			FileHelper.setFile(".\\Benchmark\\output\\" + benchmark.getName().replace(".pl", "_seminaive.pl"), 
				ASTHelper.printFacts(engine.getComputedDatabase()));
		}
	}
	
	@Test
	public void DisjunctionBenchmarkTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException, IOException {
		
		File folder = new File(".\\Benchmark\\input");
		File[] list = folder.listFiles();
		
		// Warmup
		for (int i = 0; i < threshold; i++) {
			program = FileHelper.getFile(".\\TestPrograms\\easyLoop.problog");
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.init(validProgram);
		}
		
		for (File benchmark : list) {
			program = FileHelper.getFile(benchmark.getPath());
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.setThreshold(0.001);
			engine.setParameter(ProblogEngineFactory.Parameter.Disjunction, ParameterHelper::maximum);
			engine.init(validProgram);
			System.out.println("Disjunction - " + benchmark.getName() +"\n" + engine.getStats());
			FileHelper.setFile(".\\Benchmark\\output\\" + benchmark.getName().replace(".pl", "_disj_seminaive.pl"), 
				ASTHelper.printFacts(engine.getComputedDatabase()));
		}
	}
	
	@Test
	public void ConjunctionBenchmarkTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException, IOException {
		
		File folder = new File(".\\Benchmark\\input");
		File[] list = folder.listFiles();
		
		// Warmup
		for (int i = 0; i < threshold; i++) {
			program = FileHelper.getFile(".\\TestPrograms\\easyLoop.problog");
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.init(validProgram);
		}
		
		for (File benchmark : list) {
			program = FileHelper.getFile(benchmark.getPath());
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.setThreshold(0.001);
			engine.setParameter(ProblogEngineFactory.Parameter.Conjunction, ParameterHelper::product);
			engine.init(validProgram);
			System.out.println("Conjunction - " + benchmark.getName() +"\n" + engine.getStats());
			FileHelper.setFile(".\\Benchmark\\output\\" + benchmark.getName().replace(".pl", "_conj_seminaive.pl"), 
				ASTHelper.printFacts(engine.getComputedDatabase()));
		}
	}
	
	@Test
	public void PropagationBenchmarkTest() throws ProblogParseException, ProblogValidationException, ProblogEngineException, IOException {
		
		File folder = new File(".\\Benchmark\\input");
		File[] list = folder.listFiles();
		
		// Warmup
		for (int i = 0; i < threshold; i++) {
			program = FileHelper.getFile(".\\TestPrograms\\easyLoop.problog");
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.init(validProgram);
		}
		
		for (File benchmark : list) {
			program = FileHelper.getFile(benchmark.getPath());
			validProgram = ASTHelper.getProgram(program);
			engine = ProblogEngineFactory.createEngine(ProblogEngineFactory.Mode.SemiNaive, validProgram);
			engine.setThreshold(0.001);
			engine.setParameter(ProblogEngineFactory.Parameter.Propagation, ParameterHelper::minimum);
			engine.init(validProgram);
			System.out.println("Propagation - " + benchmark.getName() +"\n" + engine.getStats());
			FileHelper.setFile(".\\Benchmark\\output\\" + benchmark.getName().replace(".pl", "_prop_seminaive.pl"), 
				ASTHelper.printFacts(engine.getComputedDatabase()));
		}
	}
}
