% generate problem of size 10
reachable(X,Y) :- edge(X,Y) : 0.4.
reachable(X,Y) :- edge(X,Z), reachable(Z,Y) : 0.4.
same_clique(X,Y) :- reachable(X,Y), reachable(Y,X) : 0.6.
edge(0, 1) : 0.6.
edge(1, 2) : 0.6.
edge(2, 3) : 0.1.
edge(3, 4) : 0.4.
edge(4, 5) : 0.4.
edge(5, 0) : 0.1.
edge(5, 6) : 0.7.
edge(6, 7) : 0.8.
edge(7, 8) : 0.3.
edge(8, 9) : 0.2.
edge(9, 10) : 0.1.
edge(10, 7) : 0.3.
