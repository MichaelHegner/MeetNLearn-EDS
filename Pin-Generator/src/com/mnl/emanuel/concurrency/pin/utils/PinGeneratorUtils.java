package com.mnl.emanuel.concurrency.pin.utils;

import java.util.Random;

import com.mnl.emanuel.concurrency.pin.dto.Pin;
import com.mnl.emanuel.concurrency.utils.ThreadUtils;

/**
 * Pin generator Utils for generating secure pin numbers.
 * 
 * NOT IMPORTANT FOR UNDERSTANDING MULTITHREADING!!!!
 * 
 * @author Michael Hegner
 *
 */
public class PinGeneratorUtils {
	
	/**
	 * Simulates a secure pin number generation.
	 * 
	 * @param pin the pin object to fill with a secure pin number.
	 */
	public static void createRandomPin(Pin pin) {
		Random rnd = new Random();
		
		for (int i = 0; i < Pin.NUMBERS; i++) {
			ThreadUtils.safeSleep(rnd.nextInt(250)); // Simulate expensive number finding
			int randomNumber = rnd.nextInt(10);
			pin.setNumberAt(i, randomNumber);
		}
	}
	
	private PinGeneratorUtils() {
		// Utils class should not be instantiable.
	}

}
