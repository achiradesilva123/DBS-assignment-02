package com.assignment2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GETClient {

    public static void main(String[] args) {
        String server = "localhost";
        int port = 8888;

        try (Socket socket = new Socket(server, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET /weather.json HTTP/1.1");
            out.println("Host: " + server);
            out.println();

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
