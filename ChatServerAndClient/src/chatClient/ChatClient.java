package chatClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



/**
 * @author Anders, Ruben, Bastian
 */
public class ChatClient extends Thread{
    private Scanner input;
    private PrintWriter output;
    Socket socket;
    private InetAddress serverAddress;    
    private int port;
   
    public static void main(String[] args) {

    }

    public void connect(String address, int port) throws UnknownHostException, IOException{
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }
    

    public void send() {

    }

    public void stopClient() {

    }

    public void registerEchoListener() {

    }

    public void unRegisterEchoListener() {

    }

      private void notifyListeners() {
    }
  
    public void run() {
  
    }
}
