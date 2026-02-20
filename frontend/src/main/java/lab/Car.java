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

@Setter
@Getter
@AllArgsConstructor
@ToString
@Log4j2
public class Car extends Entity implements Drawable, AutoMovable, Collisionable {
    private double speed;

    public Car(Image image) {
        super(image, new Point2D(0,0), new Point2D(0,0));

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

        if (position.getX() < -150 && this.getSpeed() < 0) {
            this.setPosition(new Point2D(1100, position.getY()));
        } else if (position.getX() > 900 && this.getSpeed() > 0) {
            this.setPosition(new Point2D(-150, position.getY()));
        }
    }
}


