% product shipping example

ship_to(ProdName, City) :-
    has_ordered(CustNo, ProdNo),
    customer_city(CustNo, City),
    product_name(ProdNo, ProdName) : 1.0.

customer_city(1, london) : 1.0.
customer_city(2, paris) : 1.0.
customer_city(3, 'San Francisco') : 1.0.
customer_city(4, munich) : 1.0.
customer_city(5, seoul) : 1.0.

has_ordered(1, 1) : 1.0.
has_ordered(2, 2) : 1.0.
has_ordered(3, 3) : 1.0.
has_ordered(4, 4) : 1.0.
has_ordered(5, 5) : 1.0.

product_name(1, tea) : 1.0.
product_name(2, bread) : 1.0.
product_name(3, flowers) : 1.0.
product_name(4, sausage) : 1.0.
product_name(5, horse) : 1.0.
