package com.mnl.emanuel.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScannerApplication {
	private static final String ENTER = "ENTER";

	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Bitte Text eingeben (" + ENTER + " eingeben für Programmfortführung): ");
		
		List<String> lines = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String nextLine = scanner.nextLine();
			
			if (ENTER.equalsIgnoreCase(nextLine)) {
	            break;
	        }
			
			lines.add(nextLine);
		}
			
		scanner.close();
		
		lines.forEach(System.out::println);
	}
}
