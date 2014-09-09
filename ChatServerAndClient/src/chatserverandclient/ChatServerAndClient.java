/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chatserverandclient;

/**
 *
 * @author Anders
 */
public class ChatServerAndClient {
    private static String msg;

    /**
     * @param args the command line arguments
     */
    
    public static void connect()
    {
        System.out.println("huehehu");
    }
    
    public static void main(String[] args) {
        System.out.println("This works!");
        System.out.println(sendToAll(msg));
    }
    
    static String sendToAll(String msg){
        System.out.println(msg);
        return msg;
                
    }
}
