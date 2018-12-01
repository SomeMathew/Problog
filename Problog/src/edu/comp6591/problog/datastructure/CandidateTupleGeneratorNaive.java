package edu.comp6591.problog.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import edu.comp6591.problog.ast.Atom;
import edu.comp6591.problog.ast.Clause;

public class CandidateTupleGeneratorNaive implements ICandidateTupleGenerator {
    private List<List<Atom>> facts;
    private List<MutableInteger> nextFactsPos;
    private boolean done;
    private int size;
    private boolean failureRegistered = false;
    private int failurePosition = -1;

    /**
     * Initializes a new CandidateTupleGenerator for the rule with the possible
     * ground facts.
     * 
     * @param rule
     * @param factsRepo
     */
    public CandidateTupleGeneratorNaive(Clause rule, FactsRepository factsRepo) {
        this.size = rule.getBody().size();
        this.facts = new ArrayList<>(size);
        nextFactsPos = new ArrayList<>(size);
        List<Atom> body = rule.getBody();

        for (Atom atom : body) {
            List<Atom> candidateFacts = factsRepo.getAtomsAsList(atom.getPred());
            facts.add(candidateFacts);
            nextFactsPos.add(new MutableInteger(0));
        }

        done = facts.isEmpty() || facts.stream().anyMatch(Collection::isEmpty);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.comp6591.problog.datastructure.ICandidateTupleGenerator#next()
     */
    @Override
    public List<Atom> next() {
        ImmutableList.Builder<Atom> builder = new ImmutableList.Builder<Atom>();
        if (!done) {
            for (int i = 0; i < this.size; i++) {
                int nextPos = nextFactsPos.get(i).get();
                builder.add(facts.get(i).get(nextPos));
            }
            prepareNext();
        }
        return builder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.comp6591.problog.datastructure.ICandidateTupleGenerator#registerFail(int)
     */
    @Override
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

        while (i >= 0 && nextFactsPos.get(i).incrementAndGet() >= facts.get(i).size()) {
            i--;
        }

        if (i < 0) {
            this.done = true;
        } else {
            for (int k = i + 1; k < size; k++) {
                nextFactsPos.get(k).set(0);
            }
        }
    }
}
