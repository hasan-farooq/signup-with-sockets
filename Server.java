
/*
Name : Hasan Farooq
Roll Number : 18L-1009
HomeWork-3 : Build a login page using sockets to communicate b/w client aand server
Email : l181009@lhr.nu.edu.pk
Section :  6A
 */


/*
Server that keeps running and waiting/accepting client side messages/requests
 */

package com.company.HW3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Helper helper = new Helper();

    Server() {
        ServerSocket serverSocket;
        {
            try {
                serverSocket = new ServerSocket(4000);
                System.out.println("Started Server");
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client Conencted...");

                    Thread thread = new Thread(new ClientHandler(socket));
                    thread.start();
                }
            } catch (IOException e) { System.out.println("Server not started"); }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}