package edu.comp6591.problog.ast;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

/**
 * Represents an immutable problog clause with a certainty attached.
 * 
 * Equality between clause is defined as all parts being equal: Head, Body and
 * certainty.
 */
public class Clause implements IFact, IRule {
	private final Atom head;
	private final ImmutableList<Atom> body;
	private final Certainty certainty;
	private final boolean isGround;

	public Clause(Atom head, List<Atom> body, Certainty ruleCertainty) {
		if (head == null) {
			throw new IllegalArgumentException("The head cannot be null");
		}
		if (body == null) {
			this.body = ImmutableList.of();
		} else {
			this.body = ImmutableList.copyOf(body);
		}
		this.head = head;
		this.certainty = ruleCertainty;
		this.isGround = this.head.isGround() && this.body.stream().allMatch(Atom::isGround);
	}

	@Override
	public Atom getHead() {
		return head;
	}

	@Override
	public ImmutableList<Atom> getBody() {
		return body;
	}

	@Override
	public Certainty getCertainty() {
		return certainty;
	}

	@Override
	public boolean isGround() {
		return isGround;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((certainty == null) ? 0 : certainty.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clause other = (Clause) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (certainty == null) {
			if (other.certainty != null)
				return false;
		} else if (!certainty.equals(other.certainty))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(head);
		builder.append(" :- ");
		builder.append(Joiner.on(", ").join(body));
		builder.append(" : ");
		builder.append(certainty);
		builder.append(".");
		return builder.toString();
	}

}
