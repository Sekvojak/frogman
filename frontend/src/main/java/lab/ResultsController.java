package lab;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResultsController {

    @FXML private TableView<ResultRow> resultsTable;
    @FXML private TableColumn<ResultRow, String> playerColumn;
    @FXML private TableColumn<ResultRow, Integer> scoreColumn;
    @FXML private TableColumn<ResultRow, String> timeColumn;

    @FXML
    public void initialize() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadResultsFromBackend();
    }

    private void loadResultsFromBackend() {
        try {
            URL url = new URL("http://localhost:8080/game-results");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner sc = new Scanner(conn.getInputStream());
            StringBuilder json = new StringBuilder();
            while (sc.hasNext()) json.append(sc.nextLine());

            JSONArray arr = new JSONArray(json.toString());
            ObservableList<ResultRow> data = FXCollections.observableArrayList();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Long resultId = obj.getLong("id");
                String playerName = obj.getJSONObject("player").getString("name");
                int score = obj.getJSONObject("score").getInt("scoreValue");
                String timestamp = obj.getJSONObject("score").getString("timestamp");

                data.add(new ResultRow(resultId, playerName, score, timestamp));
            }


            resultsTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Getter
    public static class ResultRow {
        private final Long gameResultId;
        private final String playerName;
        private final int score;
        private final String timestamp;

        public ResultRow(Long gameResultId, String playerName, int score, String timestamp) {
            this.gameResultId = gameResultId;
            this.playerName = playerName;
            this.score = score;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return playerName + " - " + score;
        }
    }

    @FXML
    private void handleDeleteSelected() {
        ResultRow selected = resultsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("No row selected.");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/game-results/" + selected.getGameResultId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            if (responseCode == 204) {
                resultsTable.getItems().remove(selected);
                System.out.println("Deleted successfully.");
            } else {
                System.err.println("Failed to delete. Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
