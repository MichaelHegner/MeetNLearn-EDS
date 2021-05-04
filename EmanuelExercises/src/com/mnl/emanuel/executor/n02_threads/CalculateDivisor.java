package com.mnl.emanuel.executor.n02_threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	 * Berechnungsroutine.
	 * 
	 * Implementation with executor service and future
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	DivisorResult calculate() throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newFixedThreadPool(threadCount);
		
		List<Callable<Integer>> callables = new ArrayList<>();
		for (long i = von; i <= bis; i++) {
			callables.add(new DivisorCalculator(i));
		}
		
		long number      = Integer.MIN_VALUE;
		int  maxDivisors = Integer.MIN_VALUE; 
		List<Future<Integer>> results = es.invokeAll(callables);
		for (int i = 0; i < results.size(); i++) {
			Integer result = results.get(i).get();
			if (result > maxDivisors) {
				maxDivisors = result;
				number = i + von; // Correct index 
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


class DivisorCalculator implements Callable<Integer> {
	private long numberToFindDivisors;
	
	public DivisorCalculator(long numberToFindDivisors) {
		this.numberToFindDivisors = numberToFindDivisors;
	}
	
	@Override
	public Integer call() throws Exception {
		return this.findNumberOfDivisors();
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

}

