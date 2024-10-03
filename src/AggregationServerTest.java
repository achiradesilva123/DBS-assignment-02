import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;

public class AggregationServerTest {

    private AggregationServer server;
    private PrintWriter out;
    private BufferedReader in;

    @Before
    public void setUp() throws IOException {
        server = new AggregationServer();

        Socket mockClientSocket = new Socket("localhost", 4567);
        out = new PrintWriter(mockClientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(mockClientSocket.getInputStream()));
    }

    @Test
    public void main() {
        assertNotNull("Server should be initialized", server);
    }

    @Test
    public void clientServiceHandler() throws IOException {
        try (Socket clientSocket = new Socket("localhost", 4567)) {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String jsonData = "{\n" +
                    "      \"id\": \"IDS60901\",\n" +
                    "      \"name\": \"Adelaide (West Terrace / ngayirdapira)\",\n" +
                    "      \"state\": \"SA\",\n" +
                    "      \"time_zone\": \"CST\",\n" +
                    "      \"lat\": -34.9,\n" +
                    "      \"lon\": 138.6,\n" +
                    "      \"local_date_time\": \"15/04:00pm\",\n" +
                    "      \"air_temp\": 13.3,\n" +
                    "      \"cloud\": \"Partly cloudy\"\n" +
                    "  }";

            String putRequest = "PUT /weather.json HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + jsonData.length() + "\r\n" +
                    "\r\n" +
                    jsonData;


            out.println(putRequest);


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String responseLine = in.readLine();
            System.out.println("Response after PUT request: " + responseLine);


            assertTrue(responseLine.contains("200 OK"));


            File file = new File("weather.json");
            assertTrue(file.exists());


            StringBuilder fileContent = new StringBuilder();
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    fileContent.append(line);
                }
            }


            System.out.println("Expected JSON Data:\n" + jsonData);
            System.out.println("Actual JSON Data from file:\n" + fileContent.toString());


            assertEquals("The value in the file should match the sent JSON", jsonData.replaceAll("\\s+", ""), fileContent.toString().replaceAll("\\s+", ""));
        }
    }

    @Test
    public void getImpl() throws IOException {
        String testData = "{\"weather\": \"sunny\"}";
        server.putImpl(testData, out);

        StringWriter responseWriter = new StringWriter();
        PrintWriter mockOut = new PrintWriter(responseWriter);

        server.getImpl(mockOut);

        String response = responseWriter.toString();
        assertTrue(response.contains("200 OK"));
        assertTrue(response.contains("sunny"));
    }

    @Test
    public void putImpl() throws IOException {
        String testData = "{\"weather\": \"rainy\"}";
        server.putImpl(testData, out);

        File file = new File("weather.json");
        assertTrue(file.exists());

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder data = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }
        reader.close();

        assertEquals("The value in the file should match the test data", testData, data.toString());
    }

    private String readResponse() throws IOException {
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line).append("\n");
            if (line.isEmpty()) {
                break;
            }
        }
        return response.toString();
    }
}
