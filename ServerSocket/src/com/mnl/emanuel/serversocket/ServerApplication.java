package com.mnl.emanuel.serversocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerApplication {
	private static final int SOCKET_PORT = ServerKey.SERVER_PORT;
	
	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)) {
			System.out.println("Server is listening on port " + SOCKET_PORT);
			
			while (true) {
				
				// Start listening on given port. 
				// This method blocks current thread until a connection has made.
				Socket socket = serverSocket.accept(); 
				System.out.println("New Client connected.");
				
				// Client input
				InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				
				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);

				String text;
				do {
					text = reader.readLine();
					String reverseText = new StringBuilder(text).reverse().toString();
					writer.println("Server: " + reverseText);
				} while (!text.equals("bye"));
				
				System.out.println("Server says 'Good Bye' to client.");
				
				socket.close();
			}
			
		}
		
	}
}
