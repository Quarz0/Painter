package eg.edu.alexu.csd.oop.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class Marquee extends Rectangle {


    public boolean done;


    private ArrayList<Point> corners;


    private ArrayList<Shape> selectedShapes;


    public Marquee() {
        super();
        done = false;
        corners = new ArrayList<>();
        selectedShapes = new ArrayList<>();
    }

    public Rectangle2D getArea(ArrayList<Shape> shapes) {
        getShape();
        Rectangle2D area = null;
        for (int i = 0; i < shapes.size(); i++) {
            if (getShape().contains(shapes.get(i).getTopLeftCorner())
                    && getShape().contains(shapes.get(i).getBottomRightCorner())) {
                if (area == null) {
                    area = new java.awt.Rectangle(shapes.get(i).getTopLeftCorner());
                }
                area.add(shapes.get(i).getBottomRightCorner());
                area.add(shapes.get(i).getTopLeftCorner());
                shapes.get(i).selected = true;
                addShape(shapes.get(i));
            }
        }
        if (area != null) {
            area = area.getBounds2D();
            setTopLeftCorner(new Point((int) area.getX(), (int) area.getY()));
            setBottomRightCorner(
                    new Point((int) (area.getX() + area.getWidth()), (int) (area.getY() + area.getHeight())));
            done = true;
            return area;
        } else {
            return null;
        }
    }


    public void resizeShapes(Point prevCorner, Point newPt) {
        int diffX = newPt.x - prevCorner.x;
        int diffY = newPt.y - prevCorner.y;
        resize(getTopLeftCorner(), newPt);
        for (int i = 0; i < selectedShapes.size(); i++) {
            Point newCorner = new Point(Math.max(topLeftCorner.x, getCorner(i).x + diffX),
                    Math.max(topLeftCorner.y, getCorner(i).y + diffY));
            selectedShapes.get(i).resize(selectedShapes.get(i).getTopLeftCorner(), newCorner);
        }
    }


    public Point getCorner(int index) {
        return corners.get(index);
    }


    public void addShape(Shape shape) {
        selectedShapes.add(shape);
        corners.add(shape.getBottomRightCorner());
    }


    @Override
    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        java.awt.Shape shape = getShape();

        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
        g2.setStroke(dashed);
        g2.setColor(Color.BLUE);
        g2.draw(shape);
        Rectangle2D[] knobs = getKnobRects(shape.getBounds2D());
        for (int i = 0; i < knobs.length; i++) {
            g2.setColor(Color.BLUE);
            g2.fill(knobs[i]);
        }
    }


    @Override
    protected Rectangle2D[] getKnobRects(Rectangle2D bounds) {

        Rectangle2D[] knobs = new Rectangle2D[4];
        knobs[SE] = new Rectangle2D.Double(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(),
                KNOB_SIZE * 2, KNOB_SIZE * 2);
        knobs[NE] = knobs[NW] = knobs[SW] = knobs[SE];
        return knobs;

    }


    @Override
    public boolean inBounds(Point p) {
        Rectangle2D bounds = getShape().getBounds2D();
        Rectangle2D knob = getKnobRects(bounds)[0];
        return bounds.contains(p) || knob.contains(p);
    }

    @Override
    public void translate(int x, int y) {
        for (Shape s : selectedShapes) {
            s.translate(x, y);
        }
        topLeftCorner.x += x;
        topLeftCorner.y += y;
        bottomRightCorner.x += x;
        bottomRightCorner.y += y;
    }
}
