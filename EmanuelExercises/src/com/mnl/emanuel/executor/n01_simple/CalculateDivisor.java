package com.mnl.emanuel.executor.n01_simple;

import java.util.concurrent.ExecutionException;

/**
 * Das folgende Programm soll aus einem vorgegebene Interval von Long-Zahlen die
 * Zahl zurueckgeben, die die meisten Divisoren hat. Sollte es mehrere solche
 * Zahlen geben, so soll die kleinste dieser Zahlen ausgegeben werden.
 * 
 * Die Berechnung soll in n Threads stattfinden, die via Executor Framework
 * gesteuert werden, und sich das Problem aufteilen - jeder Thread soll eine
 * Teilmenge des Problems loesen. Verwenden Sie bitte einen FixedThreadPool und
 * implementieren Sie die Worker als Callable.
 * 
 * @author ble
 * 
 */
public class CalculateDivisor {

	long von, bis;
	int threadCount;

	/**
	 * @param von untere Intervallgrenze
	 * @param bis obere Intervallgrenze
	 * @param threadCount Anzahl der Threads, auf die das Problem aufgeteilt werden soll
	 */
	public CalculateDivisor(long von, long bis, int threadCount) {
		this.von = von;
		this.bis = bis;
		this.threadCount = threadCount;

	}

	/**
	 * Berechnungsroutine
	 * 
	 * Implementation naive first. Iterativ in a single thread.
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	DivisorResult calculate() throws InterruptedException, ExecutionException {
		long number      = Integer.MIN_VALUE;
		int  maxDivisors = Integer.MIN_VALUE; 
		
		for (long i = von; i <= bis; i++) {
			int numberOfDivisors = new DivisorCalculator(i).findNumberOfDivisors();
			if (numberOfDivisors > maxDivisors) {
				maxDivisors = numberOfDivisors;
				number = i;
			}
		}

		return new DivisorResult(number, maxDivisors);
	}
	
	

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		if (args.length != 3) {
			System.out.println("Usage: CalculateDivisor <intervalStart> <intervalEnd> <threadCount>");
			System.exit(1);
		}
		
		long von = Long.parseLong(args[0]);
		long bis = Long.parseLong(args[1]);
		int threads = Integer.parseInt(args[2]);

		CalculateDivisor cp = new CalculateDivisor(von, bis, threads);
		System.out.print("Ergebnis: " + cp.calculate());
	}

}

/**
 * H�lt das Ergebnis einer Berechnung
 * 
 * @author bele
 * 
 * 
 */
class DivisorResult {
	// das eigentlich ergebnis - die Zahl mit der max. Anzahl von Divisoren
	long result;

	// Anzahl der Divisoren von Result
	long countDiv;

	public DivisorResult(long r, long c) {
		result = r;
		countDiv = c;
	}

	public long getResult() {
		return result;
	}

	public long getCountDiv() {
		return countDiv;
	}

	@Override
	public String toString() {
		return "Zahl mit maximaler Anzahl Divisoren: " + result + " (" + countDiv + " Divisoren)";
	}

}


class DivisorCalculator {
	private long numberToFindDivisors;
	
	public DivisorCalculator(long numberToFindDivisors) {
		this.numberToFindDivisors = numberToFindDivisors;
	}

	public int findNumberOfDivisors() throws InterruptedException {
		int numberOfDivisors = 0;
		
		for (long i = 1; i <= numberToFindDivisors; i++) {
			if (numberToFindDivisors % i == 0) {
				numberOfDivisors++;
			}
		}
		
		Thread.sleep(1);
		return numberOfDivisors;
	}
	
	
	private DivisorCalculator() {}
}

