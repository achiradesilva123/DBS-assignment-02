package com.assignment2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AggregationServer {
    private static final int DEFAULT_PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.println("Aggregation Server started on port " + DEFAULT_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
