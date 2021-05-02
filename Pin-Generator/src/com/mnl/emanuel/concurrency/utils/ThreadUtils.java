package com.mnl.emanuel.concurrency.utils;

public class ThreadUtils {

	public static void safeJoin(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
