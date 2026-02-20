package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public abstract class Entity implements Drawable {
    Image image;
    Point2D position;
    Point2D size;

    @Override
    public final void draw(GraphicsContext gc) {
        gc.save();
        drawInternal(gc);
        gc.restore();
    }

    protected abstract void drawInternal(GraphicsContext gc);

}
