package edu.comp6591.problog.test;

import java.util.List;

public final class TestCandidates {
    public static String NO_UNCERTAINTY 
        = "parent(mary,anna) : 1.0."
        + "parent(john,anna) : 1.0."
        + "parent(anna,daniel) : 1.0."
        + "ancestor(X,Y) :- parent(X,Y): 1.0."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 1.0.";
    
    public static String SIMPLE_WITH_UNCERTAINTY1 
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "ancestor(X,Y) :- parent(X,Y): 1.0."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 1.0.";
    
    public static String SIMPLE_WITH_UNCERTAINTY2 
        ="parent(mary,anna) : 1.0."
        + "parent(john,anna) : 1.0."
        + "parent(anna,daniel) : 1.0."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
    
    public static String SIMPLE_WITH_UNCERTAINTY3
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
    
    public static String DUPLICATE_FACTS
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "parent(anna,daniel) : 0.4."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";

    public static String DUPLICATE_RULES
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- parent(X,Y): 0.4."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
    
    public static String CYCLIC_FACTS
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "parent(daniel,anna) : 0.4."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
    
    public static String CYCLIC_RULES 
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "ancestor(X,Y) :- parent(X,Y): 0.6."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6."
        + "ancestor(X,Y) :- parent(X,Y), ancestor(X,Y) : 0.8.";
    
    public static String MULTIPATH_FACTS
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(daniel,mary) : 0.8."
        + "parent(daniel,john) : 0.4."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.6.";
    
    public static String MULTIPATH_RULES
        = "parent(mary,anna) : 0.5."
        + "parent(john,anna) : 0.5."
        + "parent(anna,daniel) : 0.8."
        + "father(X,Y) :- parent(X,Y): 0.5."
        + "mother(X,Y) :- parent(X,Y): 0.5."
        + "ancestor(X,Y) :- parent(X,Y): 0.8."
        + "ancestor(X,Y) :- father(X,Z), ancestor(Z,Y) : 0.4."
        + "ancestor(X,Y) :- mother(X,Z), ancestor(Z,Y) : 0.6.";
    
    public static String SINGLE_ENDLESS_CYCLE
        = "e(1,1) : 0.5."
        + "e(1,2) : 0.8."
        + "p(X,Y) :- e(X,Y) : 1.0."
        + "p(X,Z) :- e(X,Y), p(Y,Z) : 0.6.";

    
    public static String[] allCandidates = { NO_UNCERTAINTY, SIMPLE_WITH_UNCERTAINTY1, SIMPLE_WITH_UNCERTAINTY2,
            SIMPLE_WITH_UNCERTAINTY3, DUPLICATE_FACTS, DUPLICATE_RULES, CYCLIC_FACTS, CYCLIC_RULES, MULTIPATH_FACTS,
            MULTIPATH_RULES, SINGLE_ENDLESS_CYCLE };
   
    private TestCandidates() {
        throw new UnsupportedOperationException("Static class");
    }
}
