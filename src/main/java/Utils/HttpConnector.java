package Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnector {
    private static final String server = "http://localhost:8080";
    private static HttpURLConnection connection;

    private static String getConnection(String server, String method, boolean doOutput, String paramsAsString, byte[] paramAsByteArray) {
        StringBuilder response = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(server).openConnection();
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setRequestMethod(method);
            connection.setDoOutput(doOutput);
            DataOutputStream out;
            if (paramsAsString != null && !paramsAsString.isEmpty()) {
                out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(paramsAsString);
                out.flush();
                out.close();
            } else {
                if (paramAsByteArray != null) {
                    out = new DataOutputStream(connection.getOutputStream());
                    out.write(paramAsByteArray);
                    out.flush();
                    out.close();
                }
            }
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            inputStreamReader.close();
            br.close();
        } catch (IOException e) {
            System.out.println("Не удалось подключиться к серверу");
            e.printStackTrace();
        }
        return response.toString();
    }

    public static boolean authentication(String mapping) {
        String link = server + "/auth";
        String serverResponse = getConnection(link, "POST", true, mapping, null);
        return Boolean.parseBoolean(serverResponse);
    }

    public static String getAll() {
        String link = server + "/getAll";
        String serverResponse = getConnection(link, "GET", false, null, null);
        return serverResponse;
    }

    public static String calcShapeArea(String json) {
        String link = server + "/calcShapeArea";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String serverResponse = getConnection(link, "POST", true, null, bytes);
        return serverResponse;
    }

    public static boolean deleteShape(String _id) {
        String link = server + "/deleteById";
        String serverResponse = getConnection(link, "DELETE", true, _id, null);
        return Boolean.parseBoolean(serverResponse);
    }

    public static void logout() {
        String link = server + "/logout";
        String serverResponse = getConnection(link, "GET", false, null, null);
    }

    public static String moveShape(String mapping) {
        String link = server + "/moveShape";
        byte[] bytes = mapping.getBytes(StandardCharsets.UTF_8);
        String serverResponse = getConnection(link, "POST", true, null, bytes);
        return serverResponse;
    }

    public static String rollShape(String mapping) {
        String link = server + "/rollShape";
        byte[] bytes = mapping.getBytes(StandardCharsets.UTF_8);
        String serverResponse = getConnection(link, "POST", true, null, bytes);
        return serverResponse;
    }

    public static String scaleShape(String mapping) {
        String link = server + "/scaleShape";
        byte[] bytes = mapping.getBytes(StandardCharsets.UTF_8);
        String serverResponse = getConnection(link, "POST", true, null, bytes);
        return serverResponse;
    }

    public static void createShape(String json) {
        String link = server + "/insertShape";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String serverResponse = getConnection(link, "PUT", true, null, bytes);
        System.out.println(serverResponse);
    }

    public static int getNewId() {
        String link = server + "/getNewId";
        String serverResponse = getConnection(link, "GET", true, null, null);
        return Integer.parseInt(serverResponse);
    }

    public static void saveUpdatedShape(String json) {
        String link = server + "/updateShape";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String serverResponse = getConnection(link, "POST", true, null, bytes);
        System.out.println(serverResponse);
    }
}
