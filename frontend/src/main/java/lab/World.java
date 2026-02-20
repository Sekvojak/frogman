package lab;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.scene.paint.Color.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Getter
@Setter
@ToString
@AllArgsConstructor
@Log4j2
public class World {

    private static final Logger logger = LogManager.getLogger(World.class);

    private final double width;
    private final double height;

    private final List<Drawable> drawables = new ArrayList<>();
    private final List<Collisionable> collisionables = new ArrayList<>();
    private final List<AutoMovable> autoMovables = new ArrayList<>();
    @Getter
    private final Frog frog;

    private int score;
    private static final Image LIFE_IMAGE = new Image(World.class.getResourceAsStream("homefrog.png"));
    private final List<ScoreListener> scoreListeners;
    private final List<TimerListener> timerListeners;
    private final List<LevelListener> levelListeners;

    // TIME LISTENER
    private int remainingTime = 24;
    private String playerName;
    int currentBestScore;

    private Stage gameStage;
    private App app;

    private boolean gameOver = false;
    private boolean gameOverShown = false;

    private int level = 1;

    public  World(double width, double height) {
        scoreListeners = new ArrayList<>();
        timerListeners = new ArrayList<>();
        levelListeners = new ArrayList<>();

        this.width = width;
        this.height = height;

        this.frog = new Frog();
        frog.addHitListener(() -> {
            if (frog.getHp() > 0) {
                frog.setHp(frog.getHp() - 1);
            } else {
                if (score > currentBestScore) {
                    // rewrite FILE
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/highestScore.txt"))){
                        bw.write(playerName + " - " + score);
                    } catch (IOException e) {
                        logger.error("Error creating a file", e);
                    }
                }
                showGameOverScreen();

            }
        });

        generateCars();
        generateLogs();
        generateTurtles();
        generateHomes();

        drawables.add(frog);
        collisionables.add(frog);
    }

    private void showGameOverScreen() {
        if (gameOverShown) {
            return;
        }
        gameOverShown = true;
        logger.info("Game over screen displayed for player: {}", playerName);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameOver.fxml"));
            Parent gameOverRoot = loader.load();

            GameOverController controller = loader.getController();
            controller.setScore(playerName, score);

            Stage primaryStage = app.getPrimaryStage();
            Scene gameOverScene = new Scene(gameOverRoot);
            primaryStage.setScene(gameOverScene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error loading gameOver.fxml", e);
        }
        try {
            Long playerId = GameResultSender.sendPlayer(playerName);
            Long scoreId = GameResultSender.sendScore(score);
            GameResultSender.sendGameResult(playerId, scoreId);
            System.out.println("Game result saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void addTimerListener(TimerListener timerListener) {
        timerListeners.add(timerListener);
    }

    public void updateTime(int time) {
        this.remainingTime = time;
        notifyTimerListeners();
    }

    private void notifyTimerListeners() {
        for (TimerListener timerListener : timerListeners) {
            timerListener.onTimeChanged(remainingTime);
        }
    }

    // SCORE LISTENER
    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }

    public void updateScore(int newScore) {
        this.score = newScore;
        notifyScoreListeners();
    }

    private void notifyScoreListeners() {
        for (ScoreListener listener : scoreListeners) {
            listener.onScoreChanged(score);
        }
    }

    // LEVEL LISTENER
    public void addLevelListener(LevelListener levelListener) {
        levelListeners.add(levelListener);
    }

    private void notifyLevelListeners() {
        for (LevelListener listener : levelListeners) {
            listener.onLevelChanged(level);
        }
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0,0,width, height);
        gc.save();

        this.drawMap(gc);
        for (Drawable drawable : drawables) {
            drawable.draw(gc);
        }

        this.drawStats(gc);
        gc.restore();
    }

    private void drawStats(GraphicsContext gc) {
        gc.setFill(WHITE);
        gc.fillRect(850, 0, 150, 550);
        gc.setFill(web("#9A0000"));
        gc.fillRect(855, 5, 140, 540);

        // SCORE
        gc.setFill(web("#B9888B"));
        gc.setFont(new Font("Calisto MT", 30));
        gc.fillText("SCORE", 870, 255);

        gc.setFill(web("#FFAB00"));
        gc.fillRect(860, 265, 130, 50);

        // LIVES
        gc.setFill(web("#B9888B"));
        gc.setFont(new Font("Calisto MT", 30));
        gc.fillText("LIVES", 870, 355);


        for (int i = 0; i < frog.getHp(); i++) {
            gc.drawImage(LIFE_IMAGE, 867.5 + i * 40, 365, 35,35);
        }

        // TIMER
        gc.setFill(web("#B9888B"));
        gc.setFont(new Font("Calisto MT", 30));
        gc.fillText("TIME", 870, 440);

        gc.setFill(web("#FFAB00"));
        gc.fillRect(860, 455, 130, 50);

        // LEVEL
        gc.setFill(web("#B9888B"));
        gc.setFont(new Font("Calisto MT", 30));
        gc.fillText("LEVEL", 870, 150);

        gc.setFill(web("#FFAB00"));
        gc.fillRect(860, 165, 130, 50);

        gc.drawImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("vsb.png"))), 865, 40, 120, 60);

    }

    private void drawMap(GraphicsContext gc) {
        gc.setFill(DARKGRAY);
        gc.fillRect(0,500, 1000, 550);
        gc.setFill(BLACK);
        gc.fillRect(0, 300, 1000, 200);
        gc.setFill(web("#FED700"));
        gc.fillRect(0,250, 1000,50);
        gc.setFill(web("#0000CC"));
        gc.fillRect(0,50,1000,200);
        gc.setFill(web("#00C500"));
        gc.fillRect(0,0,1000,50);
        gc.setFill(YELLOW);
        gc.fillRect(0, 398, 1000, 4);


        // grass polygon
        gc.setFill(web("#00C500"));
        double[] xPoints = {0,
                0, 20, 40, 70, 100, 125, 155, 180, 200, 228, 256, 290, 315, 334, 362, 395, 413,
                435, 470, 495, 513, 532, 561, 613, 623, 654, 675, 684, 700, 723, 745, 760, 786,
                800, 812, 823, 832, 845, 865, 876, 895, 912, 925, 945, 959, 972, 983, 990, 995,
                1000, 1000};  // X coordinates
        double[] yPoints = {300,
                275, 265, 275, 266, 272, 269, 263, 270, 268, 265, 275, 266, 272, 269, 263, 270, 268,
                265, 275, 266, 272, 269, 263, 270, 268, 265, 275, 266, 272, 269, 263, 270, 268,
                265, 275, 266, 272, 269, 263, 270, 268, 265, 275, 266, 272, 269, 263, 270, 268,
                275, 300 };  // Y coordinates
        gc.fillPolygon(xPoints, yPoints, xPoints.length);

        // finishes
        for (int i = 60; i <= 720; i+=165) {
            drawHome(i, gc);
        }

        // road lines
        gc.setLineWidth(4);
        gc.setStroke(YELLOW);
        gc.setLineDashes(20, 20);
        gc.strokeLine(0, 448, 1000, 448);
        gc.strokeLine(0, 348, 1000, 348);

        // reset
        gc.setLineDashes(null);
        gc.setStroke(BLACK);
        gc.setLineWidth(1);
    }

    public void drawHome(double startX, GraphicsContext gc) {
        gc.setFill(web("#0B023B"));
        double[] xPointsOfHome = {startX, startX + 10, startX + 20, startX + 30, startX + 40, startX + 50, startX + 60,
                startX + 70, startX + 80, startX + 90, startX + 100};
        double[] yPointsOfHome = {50, 30, 10, 0,  0,   0,  0,  0,  10,  30,  50};
        gc.fillPolygon(xPointsOfHome, yPointsOfHome, xPointsOfHome.length);
    }

    public void moveObjects() {
        for (AutoMovable autoMovable : autoMovables) {
            autoMovable.move();
        }
    }

    public void generateHomes() {
        double space = 165;
        for (int i = 0; i < 5; i++) {
            Home home = new Home(new Point2D(80 + (i*space), 0));
            drawables.add(home);
            collisionables.add(home);
        }
    }

    public void checkVictory() {
        if (gameOver) return;

        if (score > currentBestScore) {
            // rewrite FILE
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/highestScore.txt"))){
                bw.write(playerName + " - " + score);
            } catch (IOException e) {
                logger.error("Error creating a file", e);
            }
        }

        int completedHomes = 0;
        for (Drawable drawable : drawables) {
            if (drawable instanceof Home home && home.isCompleted()) {
                completedHomes++;
            }
        }

        if (completedHomes == 5) {
            if (level == 3) {
                gameOver = true;
                showGameOverScreen();

            } else {
                level++;
                updateScore(score + 500);
                for (Drawable drawable : drawables) {
                    if (drawable instanceof Home home) {
                        home.setShowImage(false);
                        home.setCompleted(false);
                    }
                }
                frog.setPosition(new Point2D(400,500));
                for (AutoMovable autoMovable : autoMovables) {
                    autoMovable.setSpeed(autoMovable.getSpeed() * 1.4);
                }
                notifyLevelListeners();
            }
        }
    }

    private void generateCars() {
        Image[] carImagesFromRightToLeft = {
            new Image(this.getClass().getResourceAsStream("car1.png")),
            new Image(this.getClass().getResourceAsStream("car11.png")),
            new Image(this.getClass().getResourceAsStream("car2.png")),
            new Image(this.getClass().getResourceAsStream("truck1.png")),
        };
        Image[] carImagesFromLeftToRight = {
            new Image(this.getClass().getResourceAsStream("car3.gif")),
            new Image(this.getClass().getResourceAsStream("truck2r.png")),
            new Image(this.getClass().getResourceAsStream("car4t.png")),
            new Image(this.getClass().getResourceAsStream("truck3.png")),
        };

        List<Double> car_widths_r_to_l = List.of(90.0,90.0,85.0,125.0);
        List<Double> car_widths_l_to_r = List.of(90.0,85.0,90.0,125.0,150.0);
        double carHeight = 50;

        for (int i = 0; i < 4; i++) {
            double carY = (double)250 + (i + 1) * 50;
            ArrayList<Car> cars_row = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                Car car = null;
                boolean isOverlapping = true;

                while (isOverlapping) {
                    isOverlapping = false;
                    Image carImage;
                    double carWidth = 0;
                    if (i < 2) {
                        int randomIndex = (int)(Math.random() * carImagesFromRightToLeft.length);
                        carImage = carImagesFromRightToLeft[randomIndex];
                        carWidth = car_widths_r_to_l.get(randomIndex);
                    }
                    else {
                        int randomIndex = (int)(Math.random() * carImagesFromLeftToRight.length);
                        carImage = carImagesFromLeftToRight[randomIndex];
                        carWidth = car_widths_l_to_r.get(randomIndex);
                    }

                    double carX = (double)0 + (int)(Math.random() * (900 - 100 + 1));

                    car = new Car(carImage);
                    car.setPosition(new Point2D(carX, carY));
                    car.setSize(new Point2D(carWidth, carHeight));

                    Rectangle2D curentCarBox = car.getBoundingBox();
                    for (Car existingCar : cars_row) {
                        Rectangle2D existingCarBox = existingCar.getBoundingBox();

                        if (curentCarBox.intersects(existingCarBox)) {
                            isOverlapping = true;
                            break;
                        }
                    }
                }

                if (i < 2) {
                    car.setSpeed(-1);
                }
                else {
                    car.setSpeed(1);
                }

                drawables.add(car);
                collisionables.add(car);
                autoMovables.add(car);
                cars_row.add(car);
            }
        }
    }

    private void generateLogs() {
        List<Double> logWidths = List.of(200.0, 250.0, 175.0, 225.0, 150.0);
        for (int i = 1; i <= 3; i += 2) {         // i * 50 = y coordinate
             double logY = (i * 50);
             ArrayList<Log> logs_row = new ArrayList<>();
             for (int j = 0; j < 3; j++) {
                 Log log = null;
                 boolean isOverlapping = true;

                 while (isOverlapping) {
                     isOverlapping = false;
                     double logX = (double)0 + (int)(Math.random() * (900 - 100 + 1));
                     log = new Log();
                     log.setPosition(new Point2D(logX, logY));
                     int randomIndex = (int)(Math.random() * logWidths.size());
                     log.setSize(logWidths.get(randomIndex));

                     Rectangle2D currentBoundingBox = log.getBoundingBox();
                     for (Log existingLog : logs_row) {
                         Rectangle2D existingBoundingBox = existingLog.getBoundingBox();
                         if (currentBoundingBox.intersects(existingBoundingBox)) {
                             isOverlapping = true;
                             break;
                         }
                     }
                 }
                 drawables.add(log);
                 collisionables.add(log);
                 autoMovables.add(log);
                 logs_row.add(log);
             }
        }
    }

    private void generateTurtles() {
        double groupSpacing = 350;
        double turtleSpacing = 100;

        for (int row = 0; row < 2; row++) {
            double turtleY = (double)100 + (row * 100);

            for (int group = 0; group < 3; group++) {
                int turtlesPerGroup;
                if (group % 2 == 0) {
                    turtlesPerGroup = 3;
                }
                else {
                    turtlesPerGroup = 2;
                }
                double groupStartX = group * groupSpacing;
                for (int j = 0; j < turtlesPerGroup; j++) {
                    Turtle turtle = new Turtle();
                    double turtleX = groupStartX + (j * turtleSpacing);
                    turtle.setPosition(new Point2D(turtleX, turtleY));
                    drawables.add(turtle);
                    collisionables.add(turtle);
                    autoMovables.add(turtle);
                }
            }
        }
    }

    public void checkCollision() {
        boolean isOnTurle = false;
        boolean isOnLog = false;
        Rectangle2D frogBox = frog.getBoundingBox();
        for (Collisionable collisionable : collisionables) {
            if (collisionable instanceof Car car) {
                Rectangle2D carBox = car.getBoundingBox();
                if (frogBox.intersects(carBox)) {
                    death();
                    return;
                }
            } else if (collisionable instanceof Log log) {
                Rectangle2D logBox = log.getBoundingBox();
                if (frog.getPosition().getY() == 50 || frog.getPosition().getY() == 150) {
                    double overlap = Math.min(frogBox.getMaxX(), logBox.getMaxX()) - Math.max(frogBox.getMinX(), logBox.getMinX());
                    double minimumOverlapThreshold = frogBox.getWidth() * 0.5;

                    if (frogBox.intersects(logBox) && overlap >= minimumOverlapThreshold) {
                        isOnLog = true;
                        frog.setPosition(new Point2D(frog.getPosition().getX() + log.getSpeed(), frog.getPosition().getY()));
                        if (frog.getPosition().getX() < 0) {
                            frog.setPosition(new Point2D(0, frog.getPosition().getY()));
                        }
                    }
                }
            } else if (collisionable instanceof Turtle turtle) {
                Rectangle2D turtleBox = turtle.getBoundingBox();
                if (frog.getPosition().getY() == 100 || frog.getPosition().getY() == 200) {
                    double overlap = Math.min(frogBox.getMaxX(), turtleBox.getMaxX()) - Math.max(frogBox.getMinX(), turtleBox.getMinX());
                    double minimumOverlapThreshold = frogBox.getWidth() * 0.5;

                    if (frogBox.intersects(turtleBox) && overlap >= minimumOverlapThreshold) {
                        isOnTurle = true;
                        frog.setPosition(new Point2D(frog.getPosition().getX() + turtle.getSpeed(), frog.getPosition().getY()));
                        if (frog.getPosition().getX() + frog.getSize().getX() > 850) {
                            frog.setPosition(new Point2D(800, frog.getPosition().getY()));
                        }
                    }
                }
            } else if (collisionable instanceof Home home) {
                Rectangle2D homeBox = home.getBoundingBox();
                double overlap = Math.min(frogBox.getMaxX(), homeBox.getMaxX()) - Math.max(frogBox.getMinX(), homeBox.getMinX());
                double minimumOverlapThreshold = frogBox.getWidth() * 0.7;
                if (frogBox.intersects(homeBox) && overlap >= minimumOverlapThreshold) {
                    home.setShowImage(true);
                    frog.setPosition(new Point2D(width / 2 - 100, 500));
                    updateScore(score + 100 + (remainingTime * 10));
                    updateTime(24);
                    maxY = 500;
                }
                else {
                    if (frog.getPosition().getY() == 0) {
                        frog.setPosition(new Point2D(width / 2 - 100, 500));
                    }
                }
            }
        }

        if ((frog.getPosition().getY() == 50 || frog.getPosition().getY() == 150) && !isOnLog) {
            death();
        }
        if ((frog.getPosition().getY() == 100 || frog.getPosition().getY() == 200) && !isOnTurle) {
            death();
        }
    }

    double maxY = 500;
    public void updateScore() {
        if (frog.getPosition().getY() < maxY) {
            updateScore(score + 10);
            maxY = frog.getPosition().getY();
        }
    }

    public void death() {
        frog.decreaseHP();
        updateTime(24);
        frog.setPosition(new Point2D(width/2 - 100, 500));
    }


}
