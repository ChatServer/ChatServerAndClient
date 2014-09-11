package chatServer;

//import com.sun.javafx.Utils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;
import webserver.WebServer;
import static webserver.WebServer.updateOnline;

public class ChatServer implements Runnable {
//    private static Utils ut = new Utils();

    private boolean running = true; // Til while(running) i Run
    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");

    //private List<ServerHandler> handlers = Collections.synchronizedList(new ArrayList<ServerHandler>());
    public static Map<String, ClientHandler> userMap = Collections.synchronizedMap(new HashMap<String, ClientHandler>());
    
    public static void main(String[] args) {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        String logFile = properties.getProperty("logFile");
        new ChatServer().runServer(logFile, ip, port);
    }

//    public ClientHandler sendTo(String name, String chatMessage){
//        ClientHandler = userMap.get(name);
//        return ClientHandler;
//    }
    public void stopServer() {
        running = false;
    }

// public synchronized void sendTo(ClientHandler currentHandler, String sending){
//
//     currentHandler.send(sending);  // //// which socket! 
//          
//          
//  }
    public synchronized void sendTo(String modtager, String sending) {

        // //// which socket! 
        if (modtager.equals("*")) {
            for (ClientHandler handler : userMap.values()) {
                handler.send(sending);
            }            
        }
        
    }
    
    private void runServer(String logFile, String ip, int port) {
        Utils.setLogFile(logFile, ChatServer.class.getName());
        
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Sever started");
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                //socket.setSoTimeout(60*60*30);
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");
                new ClientHandler(socket, this).start();
            } while (running == true);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Utils.closeLogger(ChatServer.class.getName());
        }
        
    }
    
    @Override
    public void run() {
        try (ServerSocket listener = new ServerSocket()) {
            // listener arguments
            while (running) {
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void removeHandler(String user) {
        userMap.remove(user);
        updateOnline();
    }
    
    void connect(String name, ClientHandler sh) {
        userMap.put(name, sh);
        String onlineMessage = "ONLINE#";
        System.out.println("connect");
        for (String userName : userMap.keySet()) {
            
            onlineMessage += userName + ",";  //Et komma for meget ofølge protokollen.
            
        }
        for (ClientHandler h : userMap.values()) {
            h.send(onlineMessage);
            System.out.println("xxx");
        }
        
    }
}
