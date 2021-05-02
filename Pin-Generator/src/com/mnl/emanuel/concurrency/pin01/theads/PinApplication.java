package com.mnl.emanuel.concurrency.pin01.theads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mnl.emanuel.concurrency.pin.dto.Pin;
import com.mnl.emanuel.concurrency.pin.utils.BruteForceUtils;
import com.mnl.emanuel.concurrency.pin.utils.PinGeneratorUtils;

public class PinApplication {
	private List<Pin> pinNumbers = new ArrayList<>();

	/**
	 * Execute pin application.
	 * 
	 * @throws InterruptedException 
	 */
	private void execute() throws InterruptedException {
		
		// Define Runnable Applications
		PinGenerator pinGenerator = new PinGenerator(pinNumbers);
		BruteForce bruteForceApp  = new BruteForce(pinNumbers);
		
		// Run applications in separate Threads
		
		// - Generator app
		Thread pinGeneratorThread = new Thread(pinGenerator);
		pinGeneratorThread.start();
		
		// - Brute force apps
		Thread bruteForceThread1 = new Thread(bruteForceApp, "Bruteforce-App-01");
		bruteForceThread1.start();
		Thread bruteForceThread2 = new Thread(bruteForceApp, "Bruteforce-App-02");
		bruteForceThread2.start();
		Thread bruteForceThread3 = new Thread(bruteForceApp, "Bruteforce-App-03");
		bruteForceThread3.start();
		
		pinGeneratorThread.join();
		
		bruteForceThread1.interrupt();
		bruteForceThread2.interrupt();
		bruteForceThread3.interrupt();
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("START APPLICATION");
		new PinApplication().execute();
		System.out.println("FINISH APPLICATION");
	}
}

/**
 * Pin generator application.
 * 
 * @author Michael Hegner
 *
 */
class PinGenerator implements Runnable {
	private static final String OUTPUT_MSG_TEMPLATE = "Create Random Pin at %s: %s";
	private static final int NUMBER_OF_PIN_GENERATIONS = 50;
	
	/**
	 * Shared resource of pin numbers.
	 */
	private List<Pin> pinNumbers;

	public PinGenerator(List<Pin> pinNumbers) {
		this.pinNumbers = pinNumbers;
	}

	@Override
	public void run() {
		int index = 0;
		
		try {
			while (thradIsNotInterrupted() && morePinsRequired()) {
				Pin pin = new Pin();
				PinGeneratorUtils.createRandomPin(pin);
				
				synchronized (pinNumbers) {
					pinNumbers.add(index, pin);
					System.out.println(String.format(OUTPUT_MSG_TEMPLATE, index, pin.getPinNumber()));
					pinNumbers.notifyAll(); // Notify all waiting threads that a new number has been generated.
				}
				
				index++;
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " is interupted!");
		}
	}

	private boolean thradIsNotInterrupted() {
		return !Thread.currentThread().isInterrupted();
	}
	
	private boolean morePinsRequired() {
		synchronized (pinNumbers) {
			return pinNumbers.size() <= NUMBER_OF_PIN_GENERATIONS;
		}
	}
}

/**
 * Brute force attacker application.
 * 
 * @author Michael Hegner
 *
 */
class BruteForce implements Runnable {
	private static final int     MILLIS_TO_WAIT         = 2_000;
	private static AtomicInteger BRUTE_FORCE_GLOBAL_POS = new AtomicInteger(0); // Atomar variable

	private List<Pin> pinNumbers; // Shared Resource
	
	/**
	 * Constructor.
	 * 
	 * @param pinNumbers the shared resource.
	 */
	public BruteForce(List<Pin> pinNumbers) {
		this.pinNumbers = pinNumbers;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		
		try {
			while (threadIsNotInterrupted() || notAllPinsCracked() ) {
				
				Pin pin            = null;
				Integer workingPos = null;
	
				/**
				 * Synchronized Block: Keep it small and avoid expensive logic inside.
				 * (That's why brute force attack is not inside of the block.) 
				 * 
				 * For learning Reasons try out application when adding brute force attack into synchronized block.
				 */
				synchronized (pinNumbers) {
					if (notAllPinsCracked()) {
						workingPos = BRUTE_FORCE_GLOBAL_POS.getAndIncrement();
						pin = pinNumbers.get(workingPos);
					} else if (threadIsNotInterrupted()) {
						System.out.println(threadName + ": Wait for new pin number.");
						pinNumbers.wait(MILLIS_TO_WAIT);
					}
				}
	
				/**
				 * This logic is only interesting in case pin and working position have valid values. 
				 */
				if (null != pin && null != workingPos) {
					String crackedPin = BruteForceUtils.bruteForce(pin);
					System.out.println(String.format("Cracked Pin by %s at %s: %s", threadName, workingPos, crackedPin));
				}
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " is interupted!");
		}
	}

	private boolean threadIsNotInterrupted() {
		return !Thread.currentThread().isInterrupted();
	}
	
	private boolean notAllPinsCracked() {
		return BRUTE_FORCE_GLOBAL_POS.get() < pinNumbers.size();
	}
	
	
}