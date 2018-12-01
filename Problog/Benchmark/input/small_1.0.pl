% small example file

mother(claudette, ann) : 1.0.
mother(jeannette, bill) : 1.0.
father(john, ann) : 1.0.
father(john, bill) : 1.0.
father('jean-jacques', alphonse) : 1.0.
father(alphonse, mireille) : 1.0.
mother(mireille, john) : 1.0.  % married a Hollywood actor!
father(brad, john) : 1.0.

parent(X,Y) :- mother(X,Y) : 1.0.
parent(X,Y) :- father(X,Y) : 1.0.

ancestor(X,Y) :- parent(X,Y) : 1.0.
ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 1.0.
