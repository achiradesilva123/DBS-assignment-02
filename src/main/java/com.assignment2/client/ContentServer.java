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

        if (args.length < 2) {
            System.out.println("Usage: java ContentServer <server> <port>");
            return;
        }

        String server = args[0];
        int port = Integer.parseInt(args[1]);
        String data = "weather.json";


        try (Socket socket = new Socket(server, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            InputStream is = ContentServer.class.getClassLoader().getResourceAsStream(data);

            if (is == null) {
                System.out.println("File not found: " + data);
                return;
            }

             JSONObject jsonData = readDataFile(data);
             out.println("PUT /weather.json HTTP/1.1");
             out.println("Host: " + server);
             out.println("Content-Type: application/json");
             out.println("Content-Length: " + jsonData.toString().length());
             out.println();
             out.println(jsonData.toString());

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
