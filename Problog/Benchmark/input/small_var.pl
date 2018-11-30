% small example file

mother(claudette, ann) : 0.3.
mother(jeannette, bill) : 0.9.
father(john, ann) : 0.8.
father(john, bill) : 0.3.
father('jean-jacques', alphonse) : 0.6.
father(alphonse, mireille) : 0.2.
mother(mireille, john) : 0.6.  % married a Hollywood actor!
father(brad, john) : 0.1.

parent(X,Y) :- mother(X,Y) : 0.2.
parent(X,Y) :- father(X,Y) : 0.9.

ancestor(X,Y) :- parent(X,Y) : 0.6.
ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y) : 0.4.
