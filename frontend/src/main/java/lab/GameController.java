package lab;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import lombok.Getter;

public class GameController {

    @Getter
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label levelLabel;

    private App app;
    private World world;

    private boolean isAppSet = false;
    @FXML
    void initialize() {
        /**/
    }

    public void updateScore(int score) {
        if (scoreLabel != null) {
            scoreLabel.setText(String.valueOf(score));
        }
    }

    public void updateLevel(int level) {
        if (levelLabel != null) {
            levelLabel.setText("" + level);
        }
    }

    public void updateTime(int sec) {
        if(timeLabel != null) {
            timeLabel.setText(String.valueOf(sec));
        }
    }

    public void setApp(App app) {
        this.app = app;
        isAppSet = true;
        startGame();
    }


    private void startGame() {
        if (isAppSet) {
            world = app.getDrawingThread().getWorld();
            if (world != null) {
                world.addScoreListener(this::updateScore);
                System.out.println("Score listener added");
                world.addTimerListener(this::updateTime);
                System.out.println("Timer listener added");
                world.addLevelListener(this::updateLevel);
                System.out.println("Level listener added");
            } else {
                System.out.println("World is null");
            }
        }
    }

}
