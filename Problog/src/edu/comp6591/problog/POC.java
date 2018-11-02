package edu.comp6591.problog;

import java.io.StringReader;
import java.util.Set;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.validation.DatalogValidationException;
import abcdatalog.ast.validation.DatalogValidator;
import abcdatalog.ast.validation.UnstratifiedProgram;
import abcdatalog.parser.DatalogParser;
import abcdatalog.parser.DatalogTokenizer;

public class POC {
	public static void main(String[] args) throws Exception {
		DatalogTokenizer t;
		Set<Clause> ast;
		UnstratifiedProgram validProgram;
		String program;
		
		program = "edge(0,1) : 0.5. edge(1,2) : 0.5. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle :- tc(X,X) : 0.3.";
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		
		validProgram = new DatalogValidator().withUncertainty().validate(ast);
		System.out.println(ast);
		
		program = "edge(0,1) : 0.5. edge(1,2) : 1.25. tc(X,Y) :- edge(X,Y) : 0.1."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y) : 0.25. cycle :- tc(X,X) : 0.3.";
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		
		try {
			validProgram = new DatalogValidator().withUncertainty().validate(ast);
		} catch (DatalogValidationException e) {
			System.err.println("INVALID: " + e.getMessage() + "\nClauses: " + ast);
		}
		
		
		program = "edge(0,1). edge(1,2). tc(X,Y) :- edge(X,Y)."
				+ "tc(X,Y) :- edge(X,Z), tc(Z,Y). cycle :- tc(X,X).";
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		validProgram = new DatalogValidator().withUncertainty().validate(ast);
		System.out.println(ast);
		
		
		program = "lk_1(a,b). lk_1(b,c). reachable(X,Y) :- lk_1(X,Y)."
				+ "reachable(X,Y) :- lk_1(X,Z), %ignore\n not reachable(Z,Y).";
		t = new DatalogTokenizer(new StringReader(program));
		ast = DatalogParser.parseProgram(t);
		try {
			validProgram = new DatalogValidator().withUncertainty().validate(ast);
		} catch (DatalogValidationException e) {
			System.err.println("INVALID: " + e.getMessage() + "\nClauses: " + ast);
		}

		String query = "lk_1(a,X)?";
		t = new DatalogTokenizer(new StringReader(query));
		PositiveAtom goal = DatalogParser.parseQuery(t);
		System.out.println(goal);
	}
}
