package chatClient;

import chatServer.ChatServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anders, Ruben, Bastian
 */
public class ChatClient implements Runnable {

    private Scanner input;
    private PrintWriter output;
    Socket socket;
    private InetAddress serverAddress;
    private int port;

    public ChatClient() {
        
    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);

    }

    public void sendConnect(String name) {
        try {
            String protocol = "CONNECT#" + name; //IMPORTANT blocking call
            send(protocol);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendmessage(String name, String message) {
        try { 
            String protocol = "SEND#" + name + "#" + message; //IMPORTANT blocking call
            send(protocol);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendClose() {
        try {
            String protocol = "CLOSE#"; //IMPORTANT blocking cal
            send(protocol);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String protocol) throws IOException {
        if (socket.isOutputShutdown()) {
            throw new IOException("Outbound socket is closed");
        }
        output.println(protocol);
    }

    public void run() {
        String msg;
        while (true) {
            msg = input.nextLine();
            System.out.println("Got the protocol: " + msg);
        }
         /* incomming msg protocol split ","*/
        
//        public void sendMessageToGui(String Name, String msg){  METODE FRA GUI
//        textArea1.setText(Name + ": " + msg);        
//    }
        
    }
    
}