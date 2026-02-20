package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class DrawingThread extends AnimationTimer {

    private final GraphicsContext gc;
	private final World world;

	private long lastTime = 0;

    public DrawingThread(Canvas canvas, Stage stage) {
		this.gc = canvas.getGraphicsContext2D();
        this.world = new World(canvas.getWidth(), canvas.getHeight());
		this.world.setGameStage(stage);
	}

	public World getWorld() {
		return world;
	}

	/**
	  * Draws objects into the canvas. Put you code here. 
	 */
	@Override
	public void handle(long now) {

		if (lastTime == 0) {
			lastTime = now;
		}

		long elapsedNanos = now - lastTime;
		if (elapsedNanos >= 1_000_000_000L) {
			lastTime = now;
			int newTime = world.getRemainingTime() - 1;
			if (newTime <= 0) {
				world.death();
			} else {
				world.updateTime(newTime);
			}
		}

		this.world.draw(gc);
		this.world.moveObjects();
		this.world.checkCollision();
		this.world.updateScore();
		this.world.checkVictory();
	}

}

