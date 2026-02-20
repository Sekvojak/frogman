package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@ToString

public class Log extends Entity implements Drawable, AutoMovable, Collisionable {
    private double speed = -0.75;

    public Log() {
        super(new Image(Objects.requireNonNull(Log.class.getResourceAsStream("logt.png"))), new Point2D(0,0), new Point2D(0,50));
    }

    public void setSize(double x) {
        this.size = new Point2D(x, size.getY());
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getX(), size.getY());
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY(), size.getX(), size.getY());
    }

    @Override
    public void move() {
        this.setPosition(new Point2D(position.getX() + speed, position.getY()));

        if (position.getX() + size.getX() < -50) {
            this.setPosition(new Point2D(position.getX() + 1150, position.getY()));
        }
    }
}
