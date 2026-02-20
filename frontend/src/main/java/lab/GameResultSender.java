package lab;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GameResultSender {

    private static final String BACKEND_URL = "http://localhost:8080";

    public static Long sendPlayer(String playerName) throws Exception {
        URL getUrl = new URL(BACKEND_URL + "/players/name/" + playerName);
        HttpURLConnection getConn = (HttpURLConnection) getUrl.openConnection();
        getConn.setRequestMethod("GET");

        if (getConn.getResponseCode() == 200) {
            return extractId(getConn.getInputStream());
        }

        URL postUrl = new URL(BACKEND_URL + "/players");
        HttpURLConnection postConn = (HttpURLConnection) postUrl.openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type", "application/json");
        postConn.setDoOutput(true);

        String jsonInputString = "{\"name\": \"" + playerName + "\"}";
        try (OutputStream os = postConn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (postConn.getResponseCode() == 201) {
            return extractId(postConn.getInputStream());
        } else {
            throw new RuntimeException("Failed to create player. HTTP code: " + postConn.getResponseCode());
        }
    }


    public static Long sendScore(int points) throws Exception {
        URL url = new URL(BACKEND_URL + "/scores");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = "{\"scoreValue\": " + points + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 201) {
            return extractId(conn.getInputStream());
        } else {
            throw new RuntimeException("Failed to create score. HTTP code: " + responseCode);
        }
    }


    public static void sendGameResult(Long playerId, Long scoreId) throws Exception {
        URL url = new URL(BACKEND_URL + "/game-results/" + playerId + "/" + scoreId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        int responseCode = conn.getResponseCode();
        if (responseCode != 201) {
            throw new RuntimeException("Failed to create game result. HTTP code: " + responseCode);
        }
    }

    private static Long extractId(InputStream responseStream) throws Exception {
        try (Scanner scanner = new Scanner(responseStream)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            int idIndex = responseBody.indexOf("\"id\":");
            if (idIndex == -1) {
                throw new RuntimeException("ID not found in response");
            }
            int start = responseBody.indexOf(":", idIndex) + 1;
            int end = responseBody.indexOf(",", start);
            if (end == -1) {
                end = responseBody.indexOf("}", start);
            }
            String idString = responseBody.substring(start, end).trim();
            return Long.parseLong(idString);
        }
    }
}
