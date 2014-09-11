/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package execute;

import chatServer.ChatServer;
import java.io.IOException;
import webserver.WebServer;


public class Execute implements Runnable
{
    
    public static void main(String[] args) throws IOException {
        
        WebServer webServer = new WebServer();
        ChatServer chatserver = new ChatServer();
    }

    @Override
    public void run() {
        
        }
    
}
