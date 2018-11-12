package edu.comp6591.problog.datastructure;

public class MutableInteger {
	private int i;

	public MutableInteger(int initial) {
		this.i = initial;
	}

	public int incrementAndGet() {
		return ++this.i;
	}

	public int getAndIncrement() {
		return this.i++;
	}

	public int get() {
		return i;
	}

	public void set(int i) {
		this.i = i;
	}
}
