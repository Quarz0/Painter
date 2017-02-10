package eg.edu.alexu.csd.oop.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;


public abstract class Shape {

    
    protected static final int KNOB_SIZE = 6;

    
    protected static final int NONE = -1, NW = 0, SW = 1, SE = 2, NE = 3;

    
    protected Color borderColor;

    
    protected Color fillColor;

    
    protected Point topLeftCorner;

    
    protected Point bottomRightCorner;

    
    protected boolean selected;

    
    public Shape() {
        selected = false;
        borderColor = Color.BLACK;
        fillColor = null;
    }

    
    public void setTopLeftCorner(Point p) {
        topLeftCorner = p;
    }

    
    public Point getTopLeftCorner() {
        return topLeftCorner;
    }

    
    public void setBottomRightCorner(Point p) {
        bottomRightCorner = p;
    }

    
    public Point getBottomRightCorner() {
        return bottomRightCorner;
    }

    
    public void setBorderColor(Color color) {
        borderColor = color;
    }

    
    public void setFillColor(Color color) {
        fillColor = color;
    }

    
    public boolean intersect(Point p) {
        return getShape().intersects(new Rectangle2D.Double(p.x - 1, p.y - 1, 2, 2));
    }

    
    public boolean inBounds(Point p) {
        Rectangle2D bounds = getShape().getBounds2D();
        bounds = new Rectangle2D.Double(bounds.getX() - KNOB_SIZE / 2, bounds.getY() - KNOB_SIZE / 2,
                bounds.getWidth() + KNOB_SIZE, bounds.getHeight() + KNOB_SIZE);
        return bounds.contains(p);
    }

    
    public abstract java.awt.Shape getShape();

    
    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        java.awt.Shape shape = getShape();

        if (fillColor != null) {
            g2.setColor(fillColor);
            g2.fill(shape);
        }
        if (selected) {
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));
        } else {
            g2.setStroke(new BasicStroke(2.0f));
        }
        g2.setColor(borderColor);
        g2.draw(shape);

        if (selected) {
            Rectangle2D[] knobs = getKnobRects(shape.getBounds2D());
            for (int i = 0; i < knobs.length; i++) {
                g2.setColor(Color.black);
                g2.fill(knobs[i]);
            }
        }
    }

    
    protected Rectangle2D[] getKnobRects(Rectangle2D bounds) {

        Rectangle2D[] knobs = new Rectangle2D[4];
        knobs[NW] = new Rectangle2D.Double(bounds.getX() - KNOB_SIZE / 2, bounds.getY() - KNOB_SIZE / 2, KNOB_SIZE,
                KNOB_SIZE);
        knobs[SW] = new Rectangle2D.Double(bounds.getX() - KNOB_SIZE / 2,
                bounds.getY() + bounds.getHeight() - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE);
        knobs[SE] = new Rectangle2D.Double(bounds.getX() + bounds.getWidth() - KNOB_SIZE / 2,
                bounds.getY() + bounds.getHeight() - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE);
        knobs[NE] = new Rectangle2D.Double(bounds.getX() + bounds.getWidth() - KNOB_SIZE / 2,
                bounds.getY() - KNOB_SIZE / 2, KNOB_SIZE, KNOB_SIZE);
        return knobs;

    }

    
    public Point getAnchorForResize(Point mouseLocation) {
        int whichKnob = getKnobContainingPoint(mouseLocation);
        Rectangle2D bounds = getShape().getBounds2D();
        if (whichKnob == NONE)
            return null;
        switch (whichKnob) {
            case NW:
                return new Point((int) (bounds.getX() + bounds.getWidth()), (int) (bounds.getY() + bounds.getHeight()));
            case NE:
                return new Point((int) bounds.getX(), (int) (bounds.getY() + bounds.getHeight()));
            case SW:
                return new Point((int) (bounds.getX() + bounds.getWidth()), (int) bounds.getY());
            case SE:
                return new Point((int) bounds.getX(), (int) bounds.getY());
        }
        return null;
    }

    
    protected int getKnobContainingPoint(Point pt) {
        if (!selected)
            return NONE;

        Rectangle2D[] knobs = getKnobRects(getShape().getBounds2D());
        for (int i = 0; i < knobs.length; i++)
            if (knobs[i].contains(pt))
                return i;
        return NONE;
    }

    
    public void translate(int x, int y) {
        topLeftCorner.x += x;
        topLeftCorner.y += y;
        bottomRightCorner.x += x;
        bottomRightCorner.y += y;
    }

    
    public void resize(Point a, Point b) {
        boolean swapY = topLeftCorner.y > bottomRightCorner.y;
        boolean swapX = topLeftCorner.x > bottomRightCorner.x;

        topLeftCorner.x = Math.min(a.x, b.x);
        topLeftCorner.y = Math.min(a.y, b.y);
        bottomRightCorner.x = Math.max(a.x, b.x);
        bottomRightCorner.y = Math.max(a.y, b.y);

        if (swapX) {
            int temp = topLeftCorner.x;
            topLeftCorner.x = bottomRightCorner.x;
            bottomRightCorner.x = temp;
        }
        if (swapY) {
            int temp = topLeftCorner.y;
            topLeftCorner.y = bottomRightCorner.y;
            bottomRightCorner.y = temp;
        }
    }

    
    public abstract Shape clone();

}
