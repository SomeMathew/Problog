% generate problem of size 10
reachable(X,Y) :- edge(X,Y) : 0.7.
reachable(X,Y) :- edge(X,Z), reachable(Z,Y) : 0.5.
increasing(X,Y) :- edge(X,Y), lt(X,Y) : 0.8.
increasing(X,Y) :- edge(X,Z), lt(X,Z), increasing(Z,Y) : 0.3.
edge(0, 1) : 0.9.
edge(1, 2) : 0.6.
edge(2, 3) : 0.3.
edge(3, 4) : 0.3.
edge(4, 5) : 0.5.
edge(5, 6) : 0.5.
edge(6, 7) : 0.9.
edge(7, 8) : 0.8.
edge(8, 9) : 0.4.
edge(9, 10) : 0.1.
edge(10, 0) : 0.2.