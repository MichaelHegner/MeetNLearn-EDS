package com.mnl.emanuel.concurrency.pin.dto;

import java.util.Arrays;

public class Pin {
	public static final int NUMBERS = 6;
	
	private int[] number = new int[NUMBERS];
	
	public String getPinNumber() {
		return Arrays.toString(number);
	}
	
	public int getNumberAt(int pos) {
		return number[pos];
	}
	
	public void setNumberAt(int pos, int number) {
		this.number[pos] = number;
	}
}
