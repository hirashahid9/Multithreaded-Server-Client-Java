/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadedserver;

/*
Name: Hira Shahid
Roll Number: 17L-4090
AP-8A
*/

import java.io.*;
import java.net.*;
import java.util.*;

/*  This is the server side for login and registration related system.
    On each request, it makes an object of ClientResponse class and then creates a thread
    to fulfill client request
*/
public class MultiThreadedServer{

    
    public static void main(String[] args) throws IOException{
   
       try {
    ServerSocket ss=new ServerSocket(2222);
    System.out.println("Server Started");
    while(true) 
    {
        Socket s=ss.accept();
        System.out.println("Connection request received");
        ClientResponse clientSock = new ClientResponse(s);
        new Thread(clientSock).start();
    }
    }catch(Exception e)
    {
        System.out.println(e);
    }

}
}
