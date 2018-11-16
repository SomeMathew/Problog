package edu.comp6591.problog.validation;

import java.util.List;
import java.util.Set;

import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Predicate;

/**
 * A Problog program where the rules and facts have been validated
 * independently.
 */
public interface IProblogProgram {
	List<Clause> getAllClauses();

	List<Clause> getRules();

	List<Clause> getInitialFacts();

	Set<Predicate> getEdbPredicateSyms();

	Set<Predicate> getIdbPredicateSyms();
}
