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
    ClientGui gui;
    
    public ChatClient(ClientGui gui) {
        this.gui = gui;        
        
    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
            new Thread(this).start();
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
//        System.out.println(name + ": " + message);
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

    public void send(String protocol) throws IOException {   // her sendes protocol til ClientHandler!
        if (socket.isOutputShutdown()) {
            throw new IOException("Outbound socket is closed");
        }
        output.println(protocol);
    }

    public void run() {                  // --------------  // her modtages protocol fra Clienthandler!
        String msgProtocol;
        while (true) {
            msgProtocol = input.nextLine();
            System.out.println("Got the protocol: " + msgProtocol);
            String[] messageParts = msgProtocol.split("#");
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", msgProtocol));   // upper skal væk
            if (messageParts[0].equals("ONLINE")) {
                String usersOnline = messageParts[1]; // evt splittes "," her , eller i GUI
                
               String[] userOnlineParts = usersOnline.split(",");
                
                gui.updateUserList(userOnlineParts);  // metodekald til metode i Gui
            }
            if (messageParts[0].equals("MESSAGE")) {
                String fromSender = messageParts[1];
                String message = messageParts[2];
                gui.sendMessageToGui(fromSender, message);   // metodekald til metode i Gui
            }
            if (messageParts[0].equals("CLOSE")) {
                String tom = "Server says : ";  // whitespace i Gui
                String close = "You have been disconnectet from ChatServer";
                gui.sendMessageToGui(tom, close);   // metodekald til metode i Gui
            }
        }

    }

}
