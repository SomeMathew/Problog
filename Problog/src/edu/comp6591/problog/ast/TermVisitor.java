package edu.comp6591.problog.ast;

import java.util.function.Function;

public interface TermVisitor<T> {
	public T visit(Variable t);

	public T visit(Constant t);

	public static <T> TermVisitor<T> build(Function<Variable, T> variableVisitor,
			Function<Constant, T> constantVisitor) {
		return new TermVisitor<T>() {

			@Override
			public T visit(Variable t) {
				return variableVisitor.apply(t);
			}

			@Override
			public T visit(Constant t) {
				return constantVisitor.apply(t);
			}
		};
	}
}
