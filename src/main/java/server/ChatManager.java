package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Chat Manager handles adding users from the server, removing users when they enter a logoff message, and broadcasting messages to all other users.
 * Broadcasting messages is performed in it's own thread started in the constructor of this class.
 * Author: Brandon Shaffer
 * Date: 7/25/2020
 */
public class ChatManager {

	private final Map<String, UserThread> usernameToSession = new HashMap<>();

	public ChatManager(){
		new BroadcastThread().start();
	}

	private class BroadcastThread extends Thread {
		@Override
		public void run() {
			while (true) {
				synchronized (this) {
					if (!usernameToSession.isEmpty()) {
						broadcastMessages();
					}
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void broadcastMessages(){
		for (Map.Entry<String, UserThread> chatSession : usernameToSession.entrySet()) {
			List<String> messages = chatSession.getValue().getMessageQueue();
			for (String message : messages) {
				sendMessageToAll(chatSession.getKey(), message);
			}
		}
	}

	private void sendMessageToAll(String username, String message){
		for (Map.Entry<String, UserThread> chatSession : usernameToSession.entrySet()) {
			chatSession.getValue().outputMessage(Optional.ofNullable(username).map(name -> name + ": ").orElse("") + message);
		}
	}

	void addSession(ServerSocket serverSocket){
		try {
			UserThread newThread = new UserThread(serverSocket.accept());
			String newUsername = getUsername(newThread);
			usernameToSession.put(newUsername, newThread);
			newThread.start();
			sendMessageToAll(null, "User added to chat: " + newUsername);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void removeSession(String username, UserThread session){
		if(usernameToSession.containsKey(username)){
			usernameToSession.remove(username);
			session.outputMessage("Logged off.");
		}
		else{
			session.outputMessage("Username: " + username + " not found in chat room.");
		}
	}

	private String getUsername(UserThread chatThread) {
		String username;
		while (true) {
			chatThread.outputMessage("Enter a Username: ");
			username = chatThread.getInputString();
			if(usernameToSession.containsKey(username)){
				chatThread.outputMessage("User with this name already logged in, please choose a unique name.");
			}
			else{
				break;
			}
		}

		return username;
	}
}
