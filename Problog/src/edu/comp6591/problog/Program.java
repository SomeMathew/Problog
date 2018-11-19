package edu.comp6591.problog;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.engine.IProblogEngine;
import edu.comp6591.problog.engine.ProblogEngineFactory;
import edu.comp6591.problog.util.FileHelper;
import edu.comp6591.problog.validation.IProblogProgram;
import edu.comp6591.problog.util.ASTHelper;
import java.util.Scanner;
import java.util.Set;

public class Program {

	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);

			System.out.println("Please enter the mode of the processing engine (1 for naive, 2 for semi-naive):");
			String mode = scan.nextLine();
			ProblogEngineFactory.Mode flag = ProblogEngineFactory.parseMode(mode);

			System.out.println("Please enter the path of the file containing the initial facts and rules:");
			String path = scan.nextLine();
			String data = FileHelper.getFile(path);
			IProblogProgram program = ASTHelper.getProgram(data);

			IProblogEngine engine = ProblogEngineFactory.createEngine(flag, program);
			engine.init(program);

			System.out.println("The data is processed and ready to use. Would you like to print the resulting database (y/n)?");
			String print = scan.nextLine();
			switch (print.toLowerCase())
			{
				case "y":
				case "yes":
					System.out.println("\nDatabase Result:");
					engine.getComputedDatabase().entrySet().forEach(System.out::println);
					break;
			}

			System.out.println("Please enter your query (or 0 to stop):");
			String query = scan.nextLine();
			while (!query.equals("0")) {
				Atom goal = ASTHelper.getGoal(query);
				Set<Atom> results = engine.query(goal);

				System.out.println("Your result(s):");
				System.out.println(ASTHelper.setResults(goal, results));

				System.out.println("Please enter your query (or 0 to stop):");
				query = scan.nextLine();
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
