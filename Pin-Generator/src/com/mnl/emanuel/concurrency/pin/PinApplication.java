package com.mnl.emanuel.concurrency.pin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mnl.emanuel.concurrency.pin.dto.Pin;
import com.mnl.emanuel.concurrency.pin.utils.BruteForceUtils;
import com.mnl.emanuel.concurrency.pin.utils.PinGeneratorUtils;
import com.mnl.emanuel.concurrency.utils.ThreadUtils;

public class PinApplication {
	private List<Pin> pinNumbers = new ArrayList<>();

	/**
	 * Execute pin application.
	 */
	private void execute() {
		
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

	}
	
	public static void main(String[] args) {
		System.out.println("START APPLICATION");
		new PinApplication().execute();
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
		
		while (!Thread.currentThread().isInterrupted()) {
			Pin pin = new Pin();
			PinGeneratorUtils.createRandomPin(pin);
			
			synchronized (pinNumbers) {
				pinNumbers.add(index, pin);
				System.out.println(String.format(OUTPUT_MSG_TEMPLATE, index, pin.getPinNumber()));
				pinNumbers.notifyAll(); // Notify all waiting threads that a new number has been generated.
			}
			
			index++;
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
		
		while (!Thread.currentThread().isInterrupted()) {
			
			Pin pin            = null;
			Integer workingPos = null;

			/**
			 * Synchronized Block: Keep it small and avoid expensive logic inside.
			 * (That's why brute force attack is not inside of the block.) 
			 * 
			 * For learning Reasons try out application when adding brute force attack into synchronized block.
			 */
			synchronized (pinNumbers) {
				if (BRUTE_FORCE_GLOBAL_POS.get() < pinNumbers.size()) {
					workingPos = BRUTE_FORCE_GLOBAL_POS.getAndIncrement();
					pin = pinNumbers.get(workingPos);
				} else {
					System.out.println(threadName + ": Wait for new pin number.");
					ThreadUtils.safeWait(pinNumbers); // obj.wait() => wait for notification.
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
	}
	
	
}