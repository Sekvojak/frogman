package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@ToString
@Getter
@Setter
@Log4j2
public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

    private Stage primaryStage;
	private Scene menuScene;
	private Scene gameScene;
    private DrawingThread drawingThread;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		loadScenes();
		showMenu();
	}

	private void loadScenes() {
		try {
			System.out.println("Loading menu.fxml...");
			FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
			Parent menuRoot = menuLoader.load();
			System.out.println("Loaded menu.fxml successfully.");
            MenuController menuController = menuLoader.getController();
			menuController.setApp(this);
			menuScene = new Scene(menuRoot, 1000, 550);

			System.out.println("Loading game.fxml....");
			FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("game.fxml"));
			Parent gameRoot = gameLoader.load();
			System.out.println("Loaded game.fxml successfully.");
            GameController gameController = gameLoader.getController();

			Canvas gameCanvas = gameController.getGameCanvas();
			drawingThread = new DrawingThread(gameCanvas, primaryStage);
			drawingThread.getWorld().setApp(this);

			gameController.setApp(this);
			gameScene = new Scene(gameRoot, 1000, 550);

			gameScene.setOnKeyPressed(event -> handleKeyPress(event, drawingThread.getWorld()));
			gameScene.setOnKeyReleased(event -> handleKeyRelease());
		} catch (Exception e) {
			System.err.println("Error loading scenes: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void showMenu() {
		System.out.println("Displaying menu scene...");
		primaryStage.setScene(menuScene);
		primaryStage.setResizable(true);
		primaryStage.setTitle("Frog Man Game by DK");
		primaryStage.show();
	}

	public void showGame() {
		primaryStage.setScene(gameScene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Frog Man Game by DK");
		drawingThread.start();
	}

	private boolean keyPressed = false;

	private void handleKeyPress(KeyEvent event, World world) {
		if (!keyPressed) {
			keyPressed = true;

			switch (event.getCode()) {
				case UP:
					world.getFrog().moveUp();
					break;
				case DOWN:
					world.getFrog().moveDown();
					break;
				case LEFT:
					world.getFrog().moveLeft();
					break;
				case RIGHT:
					world.getFrog().moveRight();
					break;
				default:
					break;
			}
		}
	}

	private void handleKeyRelease() {
		keyPressed = false;
	}

    @Override
	public void stop() throws Exception {
		super.stop();
	}

}
