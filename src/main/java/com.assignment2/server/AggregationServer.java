package com.assignment2.server;

import com.assignment2.client.LamportClock;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AggregationServer {
    private static final int DEFAULT_PORT = 8888;

    private static LamportClock lamportClock = new LamportClock(0);

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

    public static void clientServiceHandler(Socket clientSocket) throws IOException{
        // handle get and put request here
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String requestLine = in.readLine();
        if (requestLine.startsWith("GET")) {
            getImpl(out);
        } else if (requestLine.startsWith("PUT")) {
            putImpl(in, out);
        } else {
            out.println("HTTP Request : {}, 400 Bad Request");
        }

        in.close();
        out.close();

    }

    public static void getImpl(PrintWriter out) throws IOException {

        lamportClock.increment();

        File file = new File("weather_data.json");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            reader.close();
            out.println("HTTP Get : {}, 200 OK");
            out.println("Content-Type: application/json");
            out.println();
            out.println(data.toString());
        } else {
            out.println("HTTP Get : {}, No Content");
        }


    }

    public static void putImpl(BufferedReader in, PrintWriter out) throws IOException {

        lamportClock.increment();

        StringBuilder body = new StringBuilder();
        String line;
        while (!(line = in.readLine()).isEmpty()) {
            body.append(line);
        }

        FileWriter file = new FileWriter("weather_data.json");
        file.write(body.toString());
        file.flush();
        file.close();
        out.println("HTTP PUT : {}, 200 OK");
    }
}
