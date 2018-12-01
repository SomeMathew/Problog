% generate problem of size 10
reachable(X,Y) :- edge(X,Y) : 1.0.
reachable(X,Y) :- edge(X,Z), reachable(Z,Y) : 1.0.
same_clique(X,Y) :- reachable(X,Y), reachable(Y,X) : 1.0.
edge(0, 1) : 1.0.
edge(1, 2) : 1.0.
edge(2, 3) : 1.0.
edge(3, 4) : 1.0.
edge(4, 5) : 1.0.
edge(5, 0) : 1.0.
edge(5, 6) : 1.0.
edge(6, 7) : 1.0.
edge(7, 8) : 1.0.
edge(8, 9) : 1.0.
edge(9, 10) : 1.0.
edge(10, 7) : 1.0.
