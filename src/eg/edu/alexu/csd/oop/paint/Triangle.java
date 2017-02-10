package eg.edu.alexu.csd.oop.paint;

import java.awt.Point;
import java.awt.geom.GeneralPath;


public class Triangle extends Shape implements IShape {


    GeneralPath triangle = new GeneralPath();


    public Triangle() {
        super();
    }


    @Override
    public java.awt.Shape getShape() {
        // TODO Auto-generated method stub
        if (topLeftCorner == null) {
            triangle = new GeneralPath();
            triangle.moveTo(new Point().x, new Point().y);
            triangle.lineTo(new Point().x, new Point().y);
            triangle.lineTo(new Point().x, new Point().y);
            return triangle;
        }
        if (bottomRightCorner == null) {
            triangle = new GeneralPath();
            triangle.moveTo(topLeftCorner.x, topLeftCorner.y);
            triangle.lineTo(topLeftCorner.x, topLeftCorner.y);
            triangle.lineTo(topLeftCorner.x, topLeftCorner.y);
            return triangle;
        }
        triangle = new GeneralPath();
        triangle.moveTo(topLeftCorner.x, topLeftCorner.y);
        triangle.lineTo(topLeftCorner.x, bottomRightCorner.y);
        triangle.lineTo(bottomRightCorner.x, bottomRightCorner.y);
        triangle.lineTo(topLeftCorner.x, topLeftCorner.y);
        return triangle;

    }


    @Override
    public Shape clone() {
        // TODO Auto-generated method stub
        Shape temp = new Triangle();
        temp.topLeftCorner = (Point) this.topLeftCorner.clone();
        temp.bottomRightCorner = (Point) this.bottomRightCorner.clone();
        temp.borderColor = this.borderColor;
        temp.fillColor = this.fillColor;
        temp.selected = false;
        return temp;

    }

}
