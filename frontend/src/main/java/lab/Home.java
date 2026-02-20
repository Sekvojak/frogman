package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Home extends Entity implements Drawable, Collisionable {
    private boolean showImage = false;
    @Setter
    private boolean completed = false;


    public Home(Point2D position) {
        super(new Image(Objects.requireNonNull(Home.class.getResourceAsStream("homefrog.png"))), position, new Point2D(60,50));
    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
        if (showImage) {
            completed = true;
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getX(), size.getY());
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        if (isShowImage()) {
            gc.drawImage(image, position.getX(), position.getY(), size.getX(), size.getY());
        }
    }
}
