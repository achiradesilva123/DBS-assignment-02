package com.assignment2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AggregationServer {
    private static final int DEFAULT_PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.println("Aggregation Server started on port " + DEFAULT_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start(); // allocate a thread to handle client request simultaneously
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable{

        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
          try {
              clientServiceHandler(clientSocket);
          }catch (Exception e){
              e.printStackTrace();
          }
        }
    }

    public static void clientServiceHandler(Socket clientSocket){
        // handle get and put request here

        System.out.println(clientSocket);

        // for get


        //for put

    }

    public static void getImpl(){

    }

    public static void putImpl(){

    }
}
