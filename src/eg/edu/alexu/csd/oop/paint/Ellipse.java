package eg.edu.alexu.csd.oop.paint;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class Ellipse extends Shape implements IShape {

    public Ellipse() {
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
        return new Ellipse2D.Double(topLeftCorner.x, topLeftCorner.y, major, minor);
    }

    @Override
    public Shape clone() {
        Shape temp = new Ellipse();
        temp.topLeftCorner = (Point) this.topLeftCorner.clone();
        temp.bottomRightCorner = (Point) this.bottomRightCorner.clone();
        temp.borderColor = this.borderColor;
        temp.fillColor = this.fillColor;
        temp.selected = false;
        return temp;
    }

}
