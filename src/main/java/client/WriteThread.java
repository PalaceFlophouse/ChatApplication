package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author: Brandon Shaffer
 * Date: 7/26/2020
 */
public class WriteThread extends Thread {

	private PrintWriter writer;
	private final Socket socket;

	public WriteThread(Socket socket){
		this.socket = socket;

		try {
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
		} catch (IOException ex) {
			System.out.println("Error getting output stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {

		Scanner systemIn = new Scanner(System.in);

		String text;
		do {
			text = systemIn.nextLine();
			writer.println(text);
		} while (!text.equalsIgnoreCase("logout"));

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
