package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Author: Brandon Shaffer
 * Date: 7/25/2020
 */
public class ChatClient {

	private final String hostname;
	private final int portNumber;

	public ChatClient(String hostname, int portNumber) {
		this.hostname = hostname;
		this.portNumber = portNumber;
	}

	public void execute() {

		try{

			Socket socket = new Socket(hostname, portNumber);

			System.out.println("Connected to chat server.");

			new ReadThread(socket).start();
			new WriteThread(socket).start();

		} catch (UnknownHostException e) {
			System.err.println("Server not found with " + hostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostname);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) return;

		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		ChatClient client = new ChatClient(hostname, port);
		client.execute();
	}

}
