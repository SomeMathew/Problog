package edu.comp6591.problog.datastructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import static com.google.common.base.Verify.*;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;
import edu.comp6591.problog.ast.Predicate;
import edu.comp6591.problog.datastructure.AnnotatedDualListHelper.Annotation;

public class CandidateTupleGeneratorSemiNaive implements ICandidateTupleGenerator {
    private List<AnnotatedDualListHelper<Atom>> factsAnnotated;
    private long typeAInNextTuple;

    private boolean done;
    private int size;
    private boolean failureRegistered = false;
    private int failurePosition = -1;

    /**
     * Initializes a new CandidateTupleGenerator with a restriction on the possible
     * candidate tuple to only the possible tuple with a member in the
     * restrictedAtom set.
     * 
     * @param rule
     * @param factsRepo
     * @param restrictedAtom
     */
    public CandidateTupleGeneratorSemiNaive(Clause rule, FactsRepository factsRepo,
            SetMultimap<Predicate, Atom> restrictedAtom) {
        List<Set<Atom>> restrictions = findRestrictedAtoms(rule.getBody(), restrictedAtom);

        this.size = rule.getBody().size();
        this.factsAnnotated = new ArrayList<>(size);
        this.typeAInNextTuple = 0;

        Iterator<Atom> bodyIter = rule.getBody().iterator();
        Iterator<Set<Atom>> restrictIter = restrictions.iterator();
        while (bodyIter.hasNext() && restrictIter.hasNext()) {
            Atom atom = bodyIter.next();

            Set<Atom> candidateFacts = factsRepo.getAtomsAsSet(atom.getPred());
            Set<Atom> restrictSet = restrictIter.next();

            AnnotatedDualListHelper<Atom> annotatedList = new AnnotatedDualListHelper<>(
                    ImmutableList.copyOf(restrictSet),
                    ImmutableList.copyOf(Sets.difference(candidateFacts, restrictSet)));
            factsAnnotated.add(annotatedList);

            if (!restrictSet.isEmpty()) {
                typeAInNextTuple++;
            }
        }

        done = factsAnnotated.isEmpty() || typeAInNextTuple <= 0
                || factsAnnotated.stream().anyMatch(AnnotatedDualListHelper::isEmpty);
    }

    /**
     * Build a table of the predicate found in the restrictedAtom set for each
     * position of the body.
     * 
     * @param body           The rule body to match for restricted atom
     * @param restrictedAtom Index of predicate to atom for all restricted atom
     * @return An positional List of all restricted atom that match the same body
     *         Atom's predicate. A position will have an empty set if no restricted
     *         atom matched the predicate of the body atom.
     */
    private List<Set<Atom>> findRestrictedAtoms(List<Atom> body, SetMultimap<Predicate, Atom> restrictedAtom) {
        List<Set<Atom>> restriction = new ArrayList<>();
        for (Atom atom : body) {
            restriction.add(restrictedAtom.get(atom.getPred()));
        }
        return restriction;
    }

    @Override
    public List<Atom> next() {
        ImmutableList.Builder<Atom> builder = new ImmutableList.Builder<Atom>();
        if (!done) {
            for (int i = 0; i < this.size; i++) {
                builder.add(factsAnnotated.get(i).getCurrent());
            }
            prepareNext();
        }
        return builder.build();
    }

    public void registerFail(int pos) {
        failureRegistered = true;
        failurePosition = pos;
    }

    private void prepareNext() {
        int i;
        // Select the next position to increment based on any registered unification
        // failure.
        if (failureRegistered) {
            i = failurePosition;
            failureRegistered = false;
        } else {
            i = size - 1;
        }

        // Go back in the list until the position has tuples left
        while (i >= 0 && !factsAnnotated.get(i).incrementIfHasNext()) {
            // Reset the list
            factsAnnotated.get(i).reset();
            i--;
        }

        // We're done here if we went through all possible tuples or all possible tuples
        // with restriction.
        assert typeAInNextTuple >= 0 : Strings.lenientFormat("Invariant error: typeAInNextTuple is negative. Value: ",
                typeAInNextTuple);
        if (i < 0 || typeAInNextTuple == 0) {
            this.done = true;
            return;
        }
    }

    void typeChangeListener(Annotation oldType, Annotation newType) {
        verify(oldType != newType, "Error in the AnnotatedDualList contract, the type didn't change. Old: %s, New: %s",
                oldType, newType);

        if (oldType == Annotation.TYPE_A) {
            typeAInNextTuple--;
        } else {
            typeAInNextTuple++;
        }
    }
}
