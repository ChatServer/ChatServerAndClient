

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

public class ServerHandler extends Thread {

  Scanner input;
  PrintWriter writer;
  Socket socket;
  ChatServer server;

  public ServerHandler(Socket socket, ChatServer server) throws IOException {
    input = new Scanner(socket.getInputStream());
    writer = new PrintWriter(socket.getOutputStream(), true);
    this.socket = socket;
    this.server = server;
  }
  
  
  
  public void send(String msg){
    writer.println(msg);
  }

  @Override
  public void run() {
    try{
    server.addHandler(this);
    String message = input.nextLine(); //IMPORTANT blocking call
//    String message = "CONNECT#Bastian"; //Testing
    String[] messageParts = message.split("#");
    Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));   // upper skal væk
    while (!message.equals("#STOP#")) {

      //server.sendToAll(message);  
      
// evt her protocol #SEND#tue,torben#besked..blabla 
/* evt.  splitter */

    
        
        if(messageParts[0].equals("CONNECT")){
        
            String user = messageParts[1]; // ikke declared endnu. user sendes til
        //hahsmap i server.
        server.connect(user,this);
//            System.out.println(server.userMap.get(user));  // test om key=user finder value
        }
        if(messageParts[0].equals("SEND")){
            String chatMessage = messageParts[2];
            
//            String receiver = messageParts[1];
            
            String comma = ",";
            StringTokenizer sk = new StringTokenizer(messageParts[1], comma);
            while(sk.hasMoreTokens()){
                String name = sk.nextToken();
                ServerHandler currentHandler = server.userMap.get(name);
                String sending = "MESSAGE#"+name+"#"+chatMessage;
                server.sendTo(currentHandler, sending );
//                server.sendTo(expli, chatMessage);
                System.out.println(sending);

            }
        }
    
      
      
//        if(messagePart[0])
      
      Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
      message = input.nextLine(); //IMPORTANT blocking call
    }
    }catch(NoSuchElementException  ste){
        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Socket timed out", ste);
    }
    writer.println("#STOP#");//Echo the stop message back to the client for a nice closedown
    try {
      input.close();
      writer.close();
      socket.close();
    } catch (IOException ex) {
      Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
      server.removeHandler(this);
    }
  }
}

