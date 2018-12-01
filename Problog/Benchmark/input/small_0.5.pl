% small example file

mother(claudette, ann) : 0.5.
mother(jeannette, bill) : 0.5.
father(john, ann) : 0.5.
father(john, bill) : 0.5.
father('jean-jacques', alphonse) : 0.5.
father(alphonse, mireille) : 0.5.
mother(mireille, john) : 0.5.  % married a Hollywood actor!
father(brad, john) : 0.5.

parent(X,Y) :- mother(X,Y) : 0.5.
parent(X,Y) :- father(X,Y) : 0.5.

ancestor(X,Y) :- parent(X,Y) : 0.5.
ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.5.
