package com.mnl.emanuel.concurrency.utils;

public class ThreadUtils {

	public static void safeSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void safeWait(Object obj) {
		try {
			obj.wait();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void safeJoin(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
