package edu.comp6591.problog;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineFactory;
import edu.comp6591.problog.util.FileHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.util.ASTHelper;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class Program {

	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);

			System.out.println("Please enter the path of the input file containing the initial facts and rules:");
			String input = scan.nextLine();
			String data = FileHelper.getFile(input);
			IProblogProgram program = ASTHelper.getProgram(data);

			System.out.println("Please enter the mode of the processing engine (1 for naive, 2 for semi-naive):");
			String mode = scan.nextLine();
			ProblogEngineFactory.Mode flag = ProblogEngineFactory.parseMode(mode);
			IProblogEngine engine = ProblogEngineFactory.createEngine(flag, program);
			
			System.out.println("Should the default parameter functions be overridden (yes/no)?");
			String param = scan.nextLine();
			switch (param.toLowerCase()) {
				case "yes":
				case "y":
					System.out.println("Please enter the disjunction function (1 for independence, 2 for maximum):");
					String disjunction = scan.nextLine();
					Function<Collection<Double>, Double> disjunctionFcn = ProblogEngineFactory.parseParam(ProblogEngineFactory.Parameter.Disjunction, disjunction);
					engine.setParameter(ProblogEngineFactory.Parameter.Disjunction, disjunctionFcn);

					System.out.println("Please enter the conjunction function (1 for product, 2 for minimum):");
					String conjunction = scan.nextLine();
					Function<Collection<Double>, Double> conjunctionFcn = ProblogEngineFactory.parseParam(ProblogEngineFactory.Parameter.Conjunction, conjunction);
					engine.setParameter(ProblogEngineFactory.Parameter.Conjunction, conjunctionFcn);

					System.out.println("Please enter the propagation function (1 for product, 2 for minimum):");
					String propagation = scan.nextLine();
					Function<Collection<Double>, Double> propagationFcn = ProblogEngineFactory.parseParam(ProblogEngineFactory.Parameter.Propagation, propagation);
					engine.setParameter(ProblogEngineFactory.Parameter.Propagation, propagationFcn);
					break;
			}
			
			System.out.println("Please enter the change detection threshold (e.g. 0.5, 0.1, 0.000001, or 0 for no threshold):");
			String threshold = scan.nextLine();
			engine.setThreshold(Double.parseDouble(threshold));
			
			engine.init(program);
			System.out.println("The data is processed and ready to use.");
			System.out.println(ASTHelper.printStatistics(engine.getStats()));
			
			System.out.println("Please enter the path of the output file to store the processed database (or 0 to skip, or 1 to print to console):");
			String output = scan.nextLine();
			switch (output) {
				case "0":
					break;
				case "1":
					System.out.println(ASTHelper.printFacts(engine.getComputedDatabase()));
					break;
				default:
					FileHelper.setFile(output, 
						ASTHelper.printStatistics(engine.getStats()) + "\n" + ASTHelper.printFacts(engine.getComputedDatabase()));
					break;
			}
			
			System.out.println("Please enter your query (or 0 to stop):");
			String query = scan.nextLine();
			while (!query.equals("0")) {
				try {
					Atom goal = ASTHelper.getGoal(query);
					Map<Atom, Double> results = engine.query(goal);

					if (results != null && !results.isEmpty()) {
						System.out.println("Your result(s):");
						System.out.println(ASTHelper.printFacts(results));
					} else {
						System.out.println("No result matches your query.");
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}

				System.out.println("Please enter your query (or 0 to stop):");
				query = scan.nextLine();
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
