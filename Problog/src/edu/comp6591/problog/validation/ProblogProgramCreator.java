package edu.comp6591.problog.validation;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Predicate;
import edu.comp6591.problog.ast.Variable;

import static java.util.stream.Collectors.*;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Validates a bag of problog clauses and creates a Problog program from a valid
 * bag.
 * 
 * Problog was initially using the abcDatalog Validator. However a new version
 * was necessary to accomodated the changes AST for proper implementation of the
 * multiset and semi-naive evaluation.
 * 
 * It was inspired by abcDatalog's validator thus credit is given here as the
 * original version used and as a source.
 */
public class ProblogProgramCreator {

	/**
	 * Validates that all variables appearing in the head of a clause also appear in
	 * the body.
	 * 
	 * @param clause
	 * @throws ProblogValidationException If there is an unbound variable
	 */
	private static void validateBoundVariables(Clause clause) throws ProblogValidationException {
		Set<Variable> headVariables = clause.getHead().getVariables();
		Set<Variable> bodyVariables = clause.getBody().stream().flatMap((atom) -> atom.getVariables().stream())
				.collect(toSet());

		// Check that all head variables are bound (used in the body)
		headVariables.removeAll(bodyVariables);
		if (!headVariables.isEmpty()) {
			throw new ProblogValidationException(
					"Every variables in the head must appear in the body of a clause.\nUnbound variables: "
							+ headVariables.toString());
		}
	}

	/**
	 * Validate that there is no 0-ary predicates.
	 * 
	 * @param clause
	 * @throws ProblogValidationException if a 0-ary predicate is present
	 */
	private static void validatePredicates(Clause clause) throws ProblogValidationException {
		if (clause.getHead().getPred().getArity() == 0) {
			throw new ProblogValidationException(
					"0-ary predicate are not allowed. Invalid: " + clause.getHead().getPred());
		}
		for (Atom atom : clause.getBody()) {
			if (atom.getPred().getArity() == 0) {
				throw new ProblogValidationException(
						"0-ary predicate are not allowed. Invalid: " + clause.getHead().getPred());
			}
		}
	}

	/**
	 * Validates that the IDB and EDB predicates are disjoints.
	 * 
	 * @param edb
	 * @param idb
	 * @throws ProblogValidationException If a predicate is found in both (Arity is
	 *                                    taken in account)
	 */
	private static void validateDisjointIdbEdb(Set<Predicate> edb, Set<Predicate> idb)
			throws ProblogValidationException {
		Sets.SetView<Predicate> intersection = Sets.intersection(edb, idb);

		if (!intersection.isEmpty()) {
			throw new ProblogValidationException(
					"Intersection of the idb and edb predicates must be empty. Invalid: " + intersection.toString());
		}
	}

	/**
	 * Validates and create a validated problog program from a list of clauses.
	 * 
	 * @param clauses
	 * @return
	 * @throws ProblogValidationException
	 */
	public static IProblogProgram create(List<Clause> clauses) throws ProblogValidationException {
		Set<Predicate> edbPredicates = new HashSet<>();
		Set<Predicate> idbPredicates = new HashSet<>();
		List<Clause> rules = new LinkedList<>();
		List<Clause> facts = new LinkedList<>();

		for (Clause c : clauses) {
			validateBoundVariables(c);
			validatePredicates(c);

			if (c.getBody().isEmpty()) {
				edbPredicates.add(c.getHead().getPred());
				facts.add(c);
			} else {
				idbPredicates.add(c.getHead().getPred());
				rules.add(c);
			}
		}

		validateDisjointIdbEdb(edbPredicates, idbPredicates);

		IProblogProgram program = new ProblogProgram(rules, facts, edbPredicates, idbPredicates);

		return program;
	}
}
