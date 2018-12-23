# Problog
Problog is a probabilistic Datalog System for the COMP6591: Introduction to Knowledge Base Systems.

## Abstract
The Problog program evaluates probabilistic datalog using a bottom-up approach to infer the IDB and its associated uncertainty of [0,1] at fixpoint. This program is composed of two evaluation engines, the naive engine that systematically calculated all possible inference outcome at each iteration and the semi-naive engine that only calculates outcome when the rule involved unify with at least one new or more certain fact. The program allows the selection of the function used by the disjunction, propagation and conjunction parameters. It also allows the selection of a cutoff threshold at which precision a change in certainty is no longer considered meaningful for the inference algorithm. The focus of the program is geared toward the correctness of the algorithm and the robustness of the structure, rather than efficiency or economy of execution. This correctness is validated through a series of unit test scenarios that are calculated both automatically and manually.

## Probabilistic Framework
The particular framework of multi-valued deductive database that is explored in this project is the parametric framework developed by Lakshmanan and Shiri [1]. This framework is devised as a generalization of deductive databases reasoning with uncertainty which can emulate other frameworks effectively. 

It is based on an IB approach, which models rules and facts in the form `A <- B1,...,Bk : a <fd,fp,fc>` where a is the certainty of the rule and the three functions represent, respectively, the disjunction, propagation and conjunction [1]. In its essence, fc merges the valuation of the body, fp applies the body’s valuation to the head and fd combines different valuation of A derived from any rule [1].

## Input Program
The system takes as its input an extended Datalog program with rules and facts of the form `A :- B1, …, Bn : a.` where: 
  - A and B are predicates with variables or constants, 
  - a is a floating point number in [0,1] and n>=0 such that n=0 represents a fact. 

The system will present a computed IDB and EDB following the evaluation of the program.


## Reference
[1] Laks V.S. Lakshmanan, Nematollaah Shiri, "A Parametric Approach to Deductive Databases with Uncertainty," IEEE Transactions on Knowledge and Data Engineering, vol. 13, 2001.
