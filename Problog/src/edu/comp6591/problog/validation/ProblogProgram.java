package edu.comp6591.problog.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Predicate;

import static java.util.stream.Collectors.*;

public class ProblogProgram implements IProblogProgram {
	private final List<Clause> rules;
	private final List<Clause> initialFacts;
	private final Set<Predicate> edbPredicateSymbols;
	private final Set<Predicate> idbPredicateSymbols;

	public ProblogProgram(List<Clause> rules, List<Clause> initialFacts, Set<Predicate> edbPredicateSymbols,
			Set<Predicate> idbPredicateSymbols) {
		this.rules = rules;
		this.initialFacts = initialFacts;
		this.edbPredicateSymbols = edbPredicateSymbols;
		this.idbPredicateSymbols = idbPredicateSymbols;
	}

	@Override
	public List<Clause> getRules() {
		return this.rules;
	}

	@Override
	public List<Clause> getInitialFacts() {
		return this.initialFacts;
	}

	public Set<Predicate> getEdbPredicateSyms() {
		return this.edbPredicateSymbols;
	}

	public Set<Predicate> getIdbPredicateSyms() {
		return this.idbPredicateSymbols;
	}

	@Override
	public List<Clause> getAllClauses() {
		return Stream.concat(rules.stream(), initialFacts.stream()).collect(toList());
	}

}
