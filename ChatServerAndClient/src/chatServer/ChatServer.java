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

public class ChatServer implements Runnable {
//    private static Utils ut = new Utils();
    private boolean running = true; // Til while(running) i Run
    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");
    
    private List<ServerHandler> handlers = Collections.synchronizedList(new ArrayList<ServerHandler>());
    Map<String,ServerHandler> userMap = Collections.synchronizedMap(new HashMap<String, ServerHandler>());
    
    
    public static void main(String[] args) {
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
    String logFile = properties.getProperty("logFile");
    new ChatServer().runServer(logFile, ip, port);
  }
    
//    public ServerHandler sendTo(String name, String chatMessage){
//        ServerHandler = userMap.get(name);
//        return ServerHandler;
//    }
    
    
    public void stopServer() {
    running = false;
    }

 public synchronized void sendTo(ServerHandler currentHandler, String sending){

     currentHandler.send(sending);  // //// which socket! 
          
          
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
                new ServerHandler(socket, this).start();
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

    void removeHandler(ServerHandler client) {
        handlers.remove(client);
    }

    void addHandler(ServerHandler client) {
        handlers.add(client);
    }

    void sendToAll(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void connect(String name, ServerHandler sh){
    userMap.put(name, sh);
    }
}
