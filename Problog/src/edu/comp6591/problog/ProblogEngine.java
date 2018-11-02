package edu.comp6591.problog;

import java.util.Set;

import abcdatalog.ast.Clause;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.validation.DatalogValidationException;
import abcdatalog.engine.DatalogEngine;

public class ProblogEngine implements DatalogEngine {

	@Override
	public void init(Set<Clause> program) throws DatalogValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<PositiveAtom> query(PositiveAtom q) {
		// TODO Auto-generated method stub
		return null;
	}

}
