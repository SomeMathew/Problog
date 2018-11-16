package edu.comp6591.problog.validation;

import java.util.List;
import java.util.Set;

import abcdatalog.ast.PredicateSym;
import edu.comp6591.problog.validation.ProblogValidatorOld.ValidProblogClause;

/**
 * A Problog program where the rules and facts have been validated
 * independently.
 * 
 * This interface differs from 
 */
public interface ProblogProgramOLD {
	List<ValidProblogClause> getAllClauses();
	
	List<ValidProblogClause> getRules();

	List<ValidProblogClause> getInitialFacts();

	Set<PredicateSym> getEdbPredicateSyms();

	Set<PredicateSym> getIdbPredicateSyms();
}
