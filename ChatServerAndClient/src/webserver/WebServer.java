/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import chatServer.ChatServer;
import static chatServer.ChatServer.userMap;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

/**
 *
 * @author Ejer
 */
public class WebServer implements Runnable{

    static int port = 8080; //not the final port
    //static String ip = "137.135.177.64"; // not the final IP
    static String ip = "localhost";

   
    
    public WebServer() throws IOException {

        InetSocketAddress i = new InetSocketAddress(ip, port); //localhost - 127.0.0.1
        HttpServer server = HttpServer.create(i, 0);

        
        server.createContext("/startpage", new genericHandler("index.html")); //Eksempel på genericHandler brugt.
        server.createContext("/logfile", new genericHandler("logfile.html"));
        server.createContext("/members", new genericHandler("groupmembers.html"));
        server.createContext("/documentation", new genericHandler("documentation.txt"));
        server.createContext("/jarfile", new genericHandler("Ca1Jar.txt"));
        
        server.createContext("/online", new onlineHandler());
        
        server.start();
        
    }

    @Override
    public void run() {
        
        
    }
    
    // Dette er en filehandler som kan bruges på alle filer.
    // Foerhen var der en handler for hver fil, men dette er der ikke brug for.
    // Vi kan nu blot kalde genericHandler, med en String fileName
    // Vi sparer tid, og kode.

    static class genericHandler implements HttpHandler {

        
        //Constructor der goer det muligt at bruge filename som input.
        
        public genericHandler(String fileName) 
        {
            this.fileName = fileName;
        }
        
        

        String content = "public/";
        String fileName;

        @Override
        public void handle(HttpExchange he) throws IOException {

            File file = new File(content + fileName);
      
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }

        }

    }
    
    
    public static void updateOnline()
    {
        new onlineHandler();
    }

    static class onlineHandler implements HttpHandler {

        
        @Override
            public void handle(HttpExchange he) throws IOException {

                StringBuilder sb = new StringBuilder();
                sb.append("<!DOCTYPE html>\n");
                sb.append("<html>\n");
                sb.append("<head>\n");
                sb.append("<title>Online Users</title>\n");
                sb.append("<meta charset='UTF-8'>\n");
                sb.append("</head>\n");
                sb.append("<body>\n");
                sb.append("<table border= 1 align='center'>");
                sb.append("<tr>");
                sb.append("<th>Name</th> ");
                sb.append("<th>Online User Count</h>");
                sb.append("</tr>");

                int userCount = 0;
                
                for (String result : userMap.keySet()) {

                    userCount = userCount +1;
                    sb.append("<tr>");
                    sb.append("<td>" + result + "</td>");
                    sb.append("<td>"+ userCount + "</td>");
                    sb.append("</tr>");

                }
                
                sb.append("<h2 align='center'>Total Count of Users at given time : " + 			userCount +"</h2>");

                sb.append("</body>\n");
                sb.append("</html>\n");
                String response = sb.toString();
                Headers h = he.getResponseHeaders();
                h.add("Content-Type", "text/html");
                he.sendResponseHeaders(200, response.length());
                try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                    pw.print(response);
                }

            }

        
        
    }

    
    
}
