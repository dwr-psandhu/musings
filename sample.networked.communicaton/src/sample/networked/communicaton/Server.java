package sample.networked.communicaton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Example of simple server that echos line back to client.
 * @author nsandhu
 *
 */
public class Server {
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(9091);
		while (!server.isClosed()) {
			final Socket clientSocket = server.accept();
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						PrintWriter out = new PrintWriter(
								clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(
								new InputStreamReader(
										clientSocket.getInputStream()));
						
						String inputLine = in.readLine();
						while(inputLine != null){
							out.println(inputLine);
							inputLine = in.readLine();
						}
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}

			}).start();
		}
	}
}
