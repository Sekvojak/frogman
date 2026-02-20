package lab;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MenuController {

    @FXML
    private TextField nameField;

    @FXML
    private Text highestScore;

    @FXML
    private Button play;

    @FXML
    private Button exit;

    @Setter
    private App app;
    private World world;

    String bestPlayer;
    int bestScore;

    @FXML
    void initialize() {
        play.setOnAction(e -> playGame());
        exit.setOnAction(e -> System.exit(0));
        try (BufferedReader br = new BufferedReader(new FileReader("src/highestScore.txt"))){
            String line;
            while ((line = br.readLine()) != null) {
                highestScore.setText("Highest score: " + line);
                String[] parts = line.split(" - ");
                bestPlayer = parts[0];
                bestScore = Integer.parseInt(parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exitGame() {
        //
    }

    @FXML
    void playGame() {
        world = app.getDrawingThread().getWorld();
        String playerName = nameField.getText();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }

        world.setPlayerName(playerName);
        app.showGame();
        world.setCurrentBestScore(bestScore);

    }

    @FXML
    void showResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("results.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Game Results");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
