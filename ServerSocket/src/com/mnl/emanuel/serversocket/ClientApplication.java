package com.mnl.emanuel.serversocket;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ClientApplication {
	private static final int    SERVER_PORT = ServerKey.SERVER_PORT;
	private static final String SERVER_HOST = ServerKey.SERVER_HOST;
	
	public static void main(String[] args) throws IOException {
		
		try (
				Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
				Scanner scanner = new Scanner(System.in);
		) {
			
			OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            String text;
            
			do {
            	System.out.print("Enter text: ");
            	text = scanner.nextLine();
 
                writer.println(text);
 
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
                String time = reader.readLine();
 
                System.out.println(time);
 
            } while (!text.equals("bye"));
			
			System.out.println("Client says 'Good Bye'");
 
		}
	}

}
