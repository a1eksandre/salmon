package net.aleksandre.salmon.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;

public class WebSocketServer {
	public static void runServer() {
		Server server = new Server("localhost", 8080, "/wsock", DBServerEndpoint.class);

		try {
			server.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Please enter \"stop\" to stop the server.");
			while (!reader.readLine().trim().equals("stop")) {
				System.out.print("Invalid command, please enter \"stop\" to stop the server.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			server.stop();
		}
	}
}
