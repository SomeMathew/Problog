package edu.comp6591.problog.datastructure;

import java.util.List;

import edu.comp6591.problog.ast.Atom;

public interface ICandidateTupleGenerator {

    /**
     * Get the next list of candidate facts for unification.
     * 
     * @return The next tuple of candidate facts, it will be empty if no more
     *         possible
     */
    List<Atom> next();

    /**
     * Registers a failure from a given predicate position in the rule generated.
     * 
     * This will skip all child of this given generated fact that caused a failure
     * in unification. Trims the space of possibility.
     * 
     * @param pos
     */
    void registerFail(int pos);

}