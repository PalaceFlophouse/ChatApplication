package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Brandon Shaffer
 * Date: 7/25/2020
 */
public class UserThread extends Thread {

	private final Socket socket;
	private List<String> messageQueue = new ArrayList<>();
	private PrintWriter out;
	private BufferedReader in;

	public UserThread(Socket socket){
		super("Chat Thread");
		this.socket = socket;

		try {
			InputStream input = socket.getInputStream();
			in = new BufferedReader(new InputStreamReader(input));

			OutputStream output = socket.getOutputStream();
			out = new PrintWriter(output, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		try {
			String inputString;
			while((inputString = in.readLine()) != null ){
				if(inputString.equalsIgnoreCase("Logout")){
					break;
				}
				messageQueue.add(inputString);
			}
			socket.close();
		} catch (IOException e) {
			System.out.println("Error in User thread.");
			e.printStackTrace();
		}
	}

	//Basic method to get the next input string from this user thread
	public String getInputString(){
		try {
			String inputString;
			while((inputString = in.readLine()) != null ){
				return inputString;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void outputMessage(String output) {
		out.println(output);
	}

	//Method for the Chat Manager to get any messages this thread has in it's queue to send to other users.
	public List<String> getMessageQueue() {
		//copy the queue and then empty it.
		List<String> outputMessages = new ArrayList<>(messageQueue);
		messageQueue = new ArrayList<>();
		return outputMessages;
	}
}
