package lab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameOverController {

    @FXML
    private Button exit;

    @FXML
    private Button playAgain;

    @FXML
    private Label scoreField;

    @FXML
    void initialize() {
        /**/
    }

    public void setScore(String playerName, int score) {
        if (scoreField != null) {
            scoreField.setText("Congratulations " + playerName+ ", your score is: " + score);
        } else {
            System.err.println("scoreField is null");
        }
    }

    @FXML
    void exitGame(ActionEvent event) {
        System.exit(1);
    }



}
