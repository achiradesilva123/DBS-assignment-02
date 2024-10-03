import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AggregationServer {
    private static final int DEFAULT_PORT = 4567;

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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void clientServiceHandler(Socket clientSocket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String requestLine = in.readLine();
        System.out.println("Request Line: " + requestLine);

        if (requestLine == null || requestLine.trim().isEmpty()) {
            System.out.println("Invalid or empty request line");
            out.println("HTTP/1.1 400 Bad Request");
            out.println();
            return;
        }

        String line;
        int contentLength = 0;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            System.out.println("Received line: " + line);
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }

        if (requestLine.startsWith("PUT")) {
            char[] body = new char[contentLength];
            in.read(body, 0, contentLength);
            String requestBody = new String(body);

            System.out.println("Received Body: " + requestBody);

            putImpl(requestBody, out);
        } else if (requestLine.startsWith("GET")) {
            getImpl(out);
        } else {
            out.println("HTTP/1.1 400 Bad Request");
        }

        in.close();
        out.close();
    }

    public static void getImpl(PrintWriter out) throws IOException {

        lamportClock.increment();

        File file = new File("weather.json");
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

    public static void putImpl(String body, PrintWriter out) throws IOException {

        lamportClock.increment();

//        StringBuilder body = new StringBuilder();
//        String line;
//        while (!(line = in.readLine()).isEmpty()) {
//            body.append(line);
//        }

        FileWriter file = new FileWriter("weather.json");
        file.write(body);
        file.flush();
        file.close();
        out.println("HTTP PUT : {}, 200 OK");
    }
}
