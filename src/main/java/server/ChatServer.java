package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Main <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        ChatManager chatManager = new ChatManager();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            System.out.println("Chat Server is listening on port " + portNumber);

            while (listening) {
                chatManager.addSession(serverSocket);

                System.out.println("New user connected");
            }

        } catch (IOException e) {
            listening = false;
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}

