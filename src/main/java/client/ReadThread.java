package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Author: Brandon Shaffer
 * Date: 7/26/2020
 */
public class ReadThread extends Thread {

	private BufferedReader reader;
	private final Socket socket;

	public ReadThread(Socket socket){
		this.socket = socket;

		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch (IOException ex) {
			System.out.println("Error getting input stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void run(){
		while(true){
			try {
				String response = reader.readLine();
				System.out.println("\n" + response);

			} catch (IOException ex) {
				System.out.println("Error reading from server: " + ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
	}
}
