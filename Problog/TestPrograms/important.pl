e(1,1) : 0.5.
e(1,2) : 0.8.
p(X,Y) :- e(X,Y) : 1.0.
p(X,Z) :- e(X,Y), p(Y,Z) : 0.6.