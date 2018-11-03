package edu.comp6591.problog;

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
			
			
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
