package eg.edu.alexu.csd.oop.paint;

import java.awt.Point;
import java.awt.geom.Line2D;

public class LineSegment extends Shape implements IShape {

    public LineSegment() {
        super();
    }

    @Override
    public java.awt.Shape getShape() {
        if (topLeftCorner == null) {
            return new Line2D.Double(new Point(), new Point());
        }
        if (bottomRightCorner == null) {
            return new Line2D.Double(topLeftCorner, topLeftCorner);
        }
        return new Line2D.Double(topLeftCorner, bottomRightCorner);
    }

    @Override
    public Shape clone() {
        Shape temp = new LineSegment();
        temp.topLeftCorner = (Point) this.topLeftCorner.clone();
        temp.bottomRightCorner = (Point) this.bottomRightCorner.clone();
        temp.borderColor = this.borderColor;
        temp.fillColor = this.fillColor;
        temp.selected = false;
        return temp;
    }

}
