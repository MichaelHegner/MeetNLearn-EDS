package com.mnl.emanuel.concurrency.pin.utils;

import java.util.Random;

import com.mnl.emanuel.concurrency.pin.dto.Pin;
import com.mnl.emanuel.concurrency.utils.ThreadUtils;

/**
 * Brute force utils class to simulate an brute force attack.
 * 
 * NOT IMPORTANT FOR UNDERSTANDING MULTITHREADING!!!!
 * 
 * @author Michael Hegner
 *
 */
public class BruteForceUtils {
	
	/**
	 * Simulate an simple brute force attack.
	 * 
	 * @param pin the pin to attack
	 * @return cracked pin
	 */
	public static String bruteForce(Pin pin) {
		Random rnd = new Random();
		String result = "";
		
		for (int i = 0; i < Pin.NUMBERS; i++) {
			int numberToFind = pin.getNumberAt(i);
		
			for (int c = 0; c < 10; c++) {
				ThreadUtils.safeSleep(rnd.nextInt(100)); // Simulate expensive brute force attack
				if (c == numberToFind) {
					result += c;
					break;
				}
			}
		}
		
		return result;
	}
	
	private BruteForceUtils() {
		// Utils class should not be instantiable.
	}

}
