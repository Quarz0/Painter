package eg.edu.alexu.csd.oop.paint;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Circle extends Ellipse implements IShape {

    public Circle() {
        super();
    }

    @Override
    public java.awt.Shape getShape() {
        if (topLeftCorner == null) {
            return new Ellipse2D.Double(0, 0, 0, 0);
        }
        if (bottomRightCorner == null) {
            return new Ellipse2D.Double(topLeftCorner.x, topLeftCorner.y, 0, 0);
        }
        int major = Math.abs(bottomRightCorner.x - topLeftCorner.x);
        int minor = Math.abs(bottomRightCorner.y - topLeftCorner.y);
        return new Ellipse2D.Double(topLeftCorner.x, topLeftCorner.y, Math.max(minor, major), Math.max(minor, major));
    }

    @Override
    public Shape clone() {
        Shape temp = new Circle();
        temp.topLeftCorner = (Point) this.topLeftCorner.clone();
        temp.bottomRightCorner = (Point) this.bottomRightCorner.clone();
        temp.borderColor = this.borderColor;
        temp.fillColor = this.fillColor;
        temp.selected = false;
        return temp;
    }

}
