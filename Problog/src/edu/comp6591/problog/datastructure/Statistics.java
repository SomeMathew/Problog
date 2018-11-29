package edu.comp6591.problog.datastructure;

public class Statistics {
	public int Iterations = 0;
	public long DurationMS = 0;
	public long EDBSize = 0;
	public long IDBSize = 0;
	
	@Override
	public String toString() {
		return "Execution time (ms): " + DurationMS + 
			", Number of iterations: " + Iterations + 
			", Size of EDB: " + EDBSize +
			", Size of IDB: " + IDBSize;
	}
}
