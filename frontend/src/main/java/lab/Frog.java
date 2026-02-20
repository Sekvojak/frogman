package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Log4j2
public class Frog extends Entity implements Drawable, Collisionable {

    private static final Logger logger = LogManager.getLogger(Frog.class);

    private final Image imageUp;
    private final Image imageDown;
    private final Image imageLeft;
    private final Image imageRight;

    private int hp = 3;
    private static final double MOVE_DISTANCE = 50;

    private List<HitListener> hitListeners;

    public Frog() {
        super(new Image(Frog.class.getResourceAsStream("frog_up.png")), new Point2D(400, 500), new Point2D(50,50));
        imageUp = new Image(this.getClass().getResourceAsStream("frog_up.png"));
        imageDown = new Image(this.getClass().getResourceAsStream("frog_down.png"));
        imageLeft = new Image(this.getClass().getResourceAsStream("frog_left.png"));
        imageRight = new Image(this.getClass().getResourceAsStream("frog_right.png"));

        this.hitListeners = new ArrayList<>();

    }

    public void addHitListener(HitListener e) {
        hitListeners.add(e);
    }

    public void removeHitListener(HitListener e) {
        hitListeners.remove(e);
    }

    public void decreaseHP() {
        for (HitListener listener : hitListeners) {
            listener.onHPChanged();
        }
    }


    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY(), size.getX(), size.getY());
    }

    public void moveRight() {
        image = imageRight;
        Point2D newPosition = new Point2D(
                Math.min(this.getPosition().getX() + MOVE_DISTANCE, 850 - this.getSize().getX()),
                this.getPosition().getY());
        this.setPosition(newPosition);

        if (logger.isTraceEnabled()) {
            logger.trace("Frog moved RIGHT to position: {}", position);
        }

    }

    public void moveLeft() {
        image = imageLeft;
        Point2D newPosition = new Point2D(
                Math.max(0, this.getPosition().getX() - MOVE_DISTANCE),
                this.getPosition().getY());
        this.setPosition(newPosition);

        if (logger.isTraceEnabled()) {
            logger.trace("Frog moved LEFT to position: {}", position);
        }
    }

    public void moveUp() {
        image = imageUp;
        Point2D newPosition = new Point2D(
                this.getPosition().getX(),
                Math.max(0, this.getPosition().getY() - MOVE_DISTANCE));
        this.setPosition(newPosition);

        if (logger.isTraceEnabled()) {
            logger.trace("Frog moved UP to position: {}", position);
        }
    }

    public void moveDown() {
        image = imageDown;
        Point2D newPosition = new Point2D(
                this.getPosition().getX(),
                Math.min(this.getPosition().getY() + MOVE_DISTANCE, 550 - this.getSize().getX()));
        this.setPosition(newPosition);

        if (logger.isTraceEnabled()) {
            logger.trace("Frog moved DOWN to position: {}", position);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getX(), size.getY());
    }
}
