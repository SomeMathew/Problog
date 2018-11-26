package edu.comp6591.problog;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.datastructure.Statistics;
import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineBase;
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
			
			System.out.println("Please enter the disjunction function (0 - default (independence), 1 - independence, 2 - minimum, 3 - maximum, 4 - product, 5 - sum, 6 - average, 7 - median):");
			String disjunction = scan.nextLine();
			Function<Collection<Double>, Double> disjunctionFcn = ProblogEngineFactory.parseParam(disjunction);
			engine.setParameter(ProblogEngineFactory.Parameter.Disjunction, disjunctionFcn);
			
			System.out.println("Please enter the conjunction function (0 - default (minimum), 1 - independence, 2 - minimum, 3 - maximum, 4 - product, 5 - sum, 6 - average, 7 - median):");
			String conjunction = scan.nextLine();
			Function<Collection<Double>, Double> conjunctionFcn = ProblogEngineFactory.parseParam(conjunction);
			engine.setParameter(ProblogEngineFactory.Parameter.Conjunction, conjunctionFcn);
			
			System.out.println("Please enter the propagation function (0 - default (product), 1 - independence, 2 - minimum, 3 - maximum, 4 - product, 5 - sum, 6 - average, 7 - median):");
			String propagation = scan.nextLine();
			Function<Collection<Double>, Double> propagationFcn = ProblogEngineFactory.parseParam(propagation);
			engine.setParameter(ProblogEngineFactory.Parameter.Disjunction, propagationFcn);
			
			engine.init(program);
			System.out.println("The data is processed and ready to use. (" + engine.getStats().DurationMS + " ms)");
			
			System.out.println("Please enter the path of the output file to store the processed database (or 0 to skip):");
			String output = scan.nextLine();
			switch (output)
			{
				case "0":
					break;
				default:
					// TODO: WRITE TO FILE STATS + COMPUTED DATABASE
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
						results.entrySet().forEach(System.out::println);
					} else {
						System.out.println("No result match your query.");
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
