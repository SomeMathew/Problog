% product shipping example

ship_to(ProdName, City) :-
    has_ordered(CustNo, ProdNo),
    customer_city(CustNo, City),
    product_name(ProdNo, ProdName) : 0.5.

customer_city(1, london) : 0.5.
customer_city(2, paris) : 0.5.
customer_city(3, 'San Francisco') : 0.5.
customer_city(4, munich) : 0.5.
customer_city(5, seoul) : 0.5.

has_ordered(1, 1) : 0.5.
has_ordered(2, 2) : 0.5.
has_ordered(3, 3) : 0.5.
has_ordered(4, 4) : 0.5.
has_ordered(5, 5) : 0.5.

product_name(1, tea) : 0.5.
product_name(2, bread) : 0.5.
product_name(3, flowers) : 0.5.
product_name(4, sausage) : 0.5.
product_name(5, horse) : 0.5.
