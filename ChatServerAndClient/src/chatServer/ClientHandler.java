package chatServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ruben, Anders, Bastian.
 */
public class ClientHandler extends Thread {

    Scanner input;
    PrintWriter writer;
    Socket socket;
    ChatServer server;

    public ClientHandler(Socket socket, ChatServer server) throws IOException {
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
        this.server = server;
    }

    public void send(String msg) {
        writer.println(msg);        
    }

    @Override
    public void run() {
        try {

            String message = input.nextLine(); //IMPORTANT blocking call
            
            String[] messageParts = message.split("#");
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));   // upper skal væk
            
            do {  
                String user = messageParts[1]; // ikke declared endnu. user sendes til hashmap i server.

                if (messageParts[0].equals("CONNECT")) {
            
                    server.connect(user, this);
//      System.out.println(server.userMap.get(user));  // test om key=user finder value
                }
                if (messageParts[0].equals("SEND")) {
                    String chatMessage = messageParts[2];
                    server.sendTo(messageParts[1], chatMessage, user);
//                    System.out.println("hgf"); // testing
//                    System.out.println(message);
                    
                }
                if (messageParts[0].equals("CLOSE")) {
                    
                    server.sendClose(user);
                    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
                }
                message = input.nextLine(); //IMPORTANT blocking call
                messageParts = message.split("#");
                
            } while (!message.equals("CLOSE"));
         
        }catch (NoSuchElementException ste) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Socket timed out", ste); 
        }
    }
}
