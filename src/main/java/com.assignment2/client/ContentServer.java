package com.assignment2.client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ContentServer {
    private static JSONObject readDataFile(String dataFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));
        String line;
        JSONObject json = new JSONObject();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            json.put(parts[0], parts[1]);
        }
        reader.close();
        return json;
    }
    public static void main(String[] args) {
        String server = "localhost";
        int port = 8888;

        try (Socket socket = new Socket(server, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("PUT Request /weather.json");
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
