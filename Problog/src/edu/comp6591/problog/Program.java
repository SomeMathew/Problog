package edu.comp6591.problog;

import abcdatalog.ast.PositiveAtom;
import edu.comp6591.problog.util.FileHelper;
import edu.comp6591.problog.util.ASTHelper;
import edu.comp6591.problog.validation.ProblogProgram;
import java.util.Scanner;

public class Program {
	
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);

			System.out.println("Please enter the path of the file containing the initial facts and rules:");
			String path = scan.nextLine();

			String data = FileHelper.getFile(path);
			ProblogProgram program = ASTHelper.getProgram(data);
			
			// DO FIXPOINT CALCULATION HERE
			
			System.out.println("The data is processed and ready to use. Please enter your query (or 0 to stop):");
			String query = scan.nextLine();
			
			while(!query.equals("0")) {
				PositiveAtom goal = ASTHelper.getGoal(query);
				
				// DO QUERY EVALUATION HERE
				
				System.out.println("OUTPUT RESULTS HERE");
				
				System.out.println("Please enter your query (or 0 to stop):");
				query = scan.nextLine();
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
