package eg.edu.alexu.csd.oop.paint;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

public class Square extends Rectangle implements IShape {

    public Square() {
        super();
    }

    @Override
    public java.awt.Shape getShape() {
        if (topLeftCorner == null) {
            return new Rectangle2D.Double(0, 0, 0, 0);
        }
        if (bottomRightCorner == null) {
            return new Rectangle2D.Double(topLeftCorner.x, topLeftCorner.y, 0, 0);
        }
        int width = Math.abs(bottomRightCorner.y - topLeftCorner.y);
        int length = Math.abs(bottomRightCorner.x - topLeftCorner.x);
        return new Rectangle2D.Double(topLeftCorner.x, topLeftCorner.y, Math.max(length, width),
                Math.max(length, width));
    }

    @Override
    public Shape clone() {
        Shape temp = new Square();
        temp.topLeftCorner = (Point) this.topLeftCorner.clone();
        temp.bottomRightCorner = (Point) this.bottomRightCorner.clone();
        temp.borderColor = this.borderColor;
        temp.fillColor = this.fillColor;
        temp.selected = false;
        return temp;
    }

}