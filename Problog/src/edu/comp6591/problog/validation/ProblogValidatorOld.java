/*******************************************************************************
 * This file is part of the AbcDatalog project.
 *
 * Copyright (c) 2016, Harvard University
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the BSD License which accompanies this distribution.
 *
 * The development of the AbcDatalog project has been supported by the 
 * National Science Foundation under Grant Nos. 1237235 and 1054172.
 *
 * See README for contributors.
 ******************************************************************************/

/**
 * Modified to support the bag model for facts needed for the implementation of Problog.
 * 
 * This modification is not backward compatible to be used by the engine of abcDatalog.
 * While it is not to be used by abcDatalog directly, it supports the validation of the 
 * same programs valid by abcDatalog.
 */
package edu.comp6591.problog.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import abcdatalog.ast.BinaryDisunifier;
import abcdatalog.ast.BinaryUnifier;
import abcdatalog.ast.Clause;
import abcdatalog.ast.Constant;
import abcdatalog.ast.Head;
import abcdatalog.ast.HeadHelpers;
import abcdatalog.ast.NegatedAtom;
import abcdatalog.ast.PositiveAtom;
import abcdatalog.ast.PredicateSym;
import abcdatalog.ast.Premise;
import abcdatalog.ast.Term;
import abcdatalog.ast.TermHelpers;
import abcdatalog.ast.Variable;
import abcdatalog.ast.visitors.CrashPremiseVisitor;
import abcdatalog.ast.visitors.HeadVisitor;
import abcdatalog.ast.visitors.HeadVisitorBuilder;
import abcdatalog.ast.visitors.PremiseVisitor;
import abcdatalog.ast.visitors.PremiseVisitorBuilder;
import abcdatalog.ast.visitors.TermVisitor;
import abcdatalog.ast.visitors.TermVisitorBuilder;
import abcdatalog.util.Box;
import abcdatalog.util.substitution.TermUnifier;
import abcdatalog.util.substitution.UnionFindBasedUnifier;

/**
 * A validator for a set of clauses in a datalog program with uncertainty. It
 * converts a set of clauses to a program, which consists of a bag of initial
 * facts and a set of rules for deriving new facts. The rules in a program are
 * guaranteed to be valid.
 */
public class ProblogValidatorOld {
	private boolean allowBinaryUnification;
	private boolean allowBinaryDisunification;
	private boolean allowNegatedBodyAtom;
	private boolean allowUncertainty;

	public ProblogValidatorOld withBinaryUnificationInRuleBody() {
		this.allowBinaryUnification = true;
		return this;
	}

	public ProblogValidatorOld withBinaryDisunificationInRuleBody() {
		this.allowBinaryDisunification = true;
		return this;
	}

	public ProblogValidatorOld withAtomNegationInRuleBody() {
		this.allowNegatedBodyAtom = true;
		return this;
	}

	public ProblogValidatorOld withUncertainty() {
		this.allowUncertainty = true;
		return this;
	}

	public ProblogProgramOLD validate(List<Clause> program) throws ProblogValidationException {
		List<ValidProblogClause> rewrittenClauses = new LinkedList<>();
		for (Clause clause : program) {
			rewrittenClauses.add(checkRule(clause));
		}
		rewrittenClauses.add(new ValidProblogClause(True.getTrueAtom(), Collections.emptyList()));

		List<ValidProblogClause> rules = new LinkedList<>();
		List<ValidProblogClause> initialFacts = new LinkedList<>();

		Set<PredicateSym> edbPredicateSymbols = new HashSet<>();
		Set<PredicateSym> idbPredicateSymbols = new HashSet<>();

		HeadVisitor<Void, PositiveAtom> getHeadAsAtom = (new HeadVisitorBuilder<Void, PositiveAtom>())
				.onPositiveAtom((atom, nothing) -> atom).orCrash();
		PremiseVisitor<Void, Void> getBodyPred = (new PremiseVisitorBuilder<Void, Void>())
				.onPositiveAtom((atom, nothing) -> {
					edbPredicateSymbols.add(atom.getPred());
					return null;
				}).onNegatedAtom((atom, nothing) -> {
					edbPredicateSymbols.add(atom.getPred());
					return null;
				}).orNull();
		for (ValidProblogClause cl : rewrittenClauses) {
			PositiveAtom head = cl.getHead().accept(getHeadAsAtom, null);
			List<Premise> body = cl.getBody();
			if (body.isEmpty()) {
				initialFacts.add(cl);
				edbPredicateSymbols.add(head.getPred());
			} else {
				idbPredicateSymbols.add(head.getPred());
				rules.add(cl);
				for (Premise c : body) {
					c.accept(getBodyPred, null);
				}
			}
		}

		edbPredicateSymbols.removeAll(idbPredicateSymbols);

		return new Program(rules, initialFacts, edbPredicateSymbols, idbPredicateSymbols);

	}

	private ValidProblogClause checkRule(Clause clause) throws ProblogValidationException {
		Set<Variable> boundVars = new HashSet<>();
		Set<Variable> possiblyUnboundVars = new HashSet<>();
		TermUnifier subst = new UnionFindBasedUnifier();

		TermVisitor<Set<Variable>, Set<Variable>> tv = (new TermVisitorBuilder<Set<Variable>, Set<Variable>>())
				.onVariable((x, set) -> {
					set.add(x);
					return set;
				}).or((x, set) -> set);

		TermHelpers.fold(HeadHelpers.forcePositiveAtom(clause.getHead()).getArgs(), tv, possiblyUnboundVars);

		Box<Boolean> hasPositiveAtom = new Box<>(false);
		PremiseVisitor<ProblogValidationException, ProblogValidationException> cv = new CrashPremiseVisitor<ProblogValidationException, ProblogValidationException>() {

			@Override
			public ProblogValidationException visit(PositiveAtom atom, ProblogValidationException e) {
				TermHelpers.fold(atom.getArgs(), tv, boundVars);
				hasPositiveAtom.value = true;
				return e;
			}

			@Override
			public ProblogValidationException visit(BinaryUnifier u, ProblogValidationException e) {
				if (!allowBinaryUnification) {
					return new ProblogValidationException("Binary unification is not allowed: ");
				}
				TermHelpers.fold(u.getArgsIterable(), tv, possiblyUnboundVars);
				TermHelpers.unify(u.getLeft(), u.getRight(), subst);
				return e;
			}

			@Override
			public ProblogValidationException visit(BinaryDisunifier u, ProblogValidationException e) {
				if (!allowBinaryDisunification) {
					return new ProblogValidationException("Binary disunification is not allowed: ");
				}
				TermHelpers.fold(u.getArgsIterable(), tv, possiblyUnboundVars);
				return e;
			}

			@Override
			public ProblogValidationException visit(NegatedAtom atom, ProblogValidationException e) {
				if (!allowNegatedBodyAtom) {
					return new ProblogValidationException("Negated body atoms are not allowed: ");
				}
				TermHelpers.fold(atom.getArgs(), tv, possiblyUnboundVars);
				return e;
			}

		};

		for (Premise c : clause.getBody()) {
			ProblogValidationException e = c.accept(cv, null);
			if (e != null) {
				throw e;
			}
		}

		for (Variable x : possiblyUnboundVars) {
			if (!boundVars.contains(x) && !(subst.get(x) instanceof Constant)) {
				throw new ProblogValidationException("Every variable in a rule must be bound, but " + x
						+ " is not bound in the rule " + clause
						+ " A variable X is bound if 1) it appears in a positive (non-negated) body atom, or 2) it is explicitly unified with a constant (e.g., X=a) or with a variable that is bound (e.g., X=Y, where Y is bound).");
			}
		}

		List<Premise> newBody = new ArrayList<>(clause.getBody());
		if (!hasPositiveAtom.value && !newBody.isEmpty()) {
			newBody.add(0, True.getTrueAtom());
		}

		if (this.allowUncertainty && (clause.getCertainty() > 1 || clause.getCertainty() < 0)) {
			throw new ProblogValidationException("The probability for uncertainty must be in [0,1] but was found to be "
					+ clause.getCertainty() + " in the rule " + clause + ".");
		}

		return new ValidProblogClause(clause.getHead(), newBody, clause.getCertainty());
	}

	private static final class Program implements ProblogProgramOLD {
		private final List<ValidProblogClause> rules;
		private final List<ValidProblogClause> initialFacts;
		private final Set<PredicateSym> edbPredicateSymbols;
		private final Set<PredicateSym> idbPredicateSymbols;

		public Program(List<ValidProblogClause> rules, List<ValidProblogClause> initialFacts,
				Set<PredicateSym> edbPredicateSymbols, Set<PredicateSym> idbPredicateSymbols) {
			this.rules = rules;
			this.initialFacts = initialFacts;
			this.edbPredicateSymbols = edbPredicateSymbols;
			this.idbPredicateSymbols = idbPredicateSymbols;
		}

		@Override
		public List<ValidProblogClause> getRules() {
			return this.rules;
		}

		@Override
		public List<ValidProblogClause> getInitialFacts() {
			return this.initialFacts;
		}

		public Set<PredicateSym> getEdbPredicateSyms() {
			return this.edbPredicateSymbols;
		}

		public Set<PredicateSym> getIdbPredicateSyms() {
			return this.idbPredicateSymbols;
		}

		@Override
		public List<ValidProblogClause> getAllClauses() {
			return Stream.concat(rules.stream(), initialFacts.stream()).collect(Collectors.toList());
		}

	}

	private final static class True {

		private True() {
			// Cannot be instantiated.
		}

		private static class TruePred extends PredicateSym {

			protected TruePred() {
				super("true", 0);
			}

		};

		private final static PositiveAtom trueAtom = PositiveAtom.create(new TruePred(), new Term[] {});

		public static PositiveAtom getTrueAtom() {
			return trueAtom;
		}
	}

	public static final class ValidProblogClause extends Clause {

		private ValidProblogClause(Head head, List<Premise> body) {
			this(head, body, 1);
		}

		public ValidProblogClause(Head head, List<Premise> body, double uncertainty) {
			super(head, body, uncertainty);
		}

	}

}
