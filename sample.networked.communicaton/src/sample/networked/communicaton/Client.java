package sample.networked.communicaton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws Exception{
		Socket clientSocket = new Socket("localhost", 9091);
		PrintWriter out = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						clientSocket.getInputStream()));
		for(int i=0; i < 10; i++){
			out.println("Hello #"+i);
			String inputLine = in.readLine();
			System.out.println("Received response: "+inputLine);
		}
	}
}
