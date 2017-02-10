package eg.edu.alexu.csd.oop.paint;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI implements ActionListener, ChangeListener {

    private static final int RESIZE = 1;

    private static final int MOVE = 2;

    private static final int MOVE_GROUP = 3;

    private static final int RESIZE_GROUP = 4;

    private static final int NONE = -1;

    private JPanel card1;

    private JFrame frame;

    private JTabbedPane tabbedPane;

    protected Canvas canvas;

    private JButton borderColor;

    private JButton fillColor;

    private JButton importShape;

    protected JButton saveJson;

    protected JButton saveJPEG;

    protected JButton saveXml;

    protected JButton load;

    protected JButton undo;

    protected JButton redo;

    private JButton delete;

    private JButton selectAll;

    private Image img;

    private JColorChooser colorChooser;

    private Object colorSource;

    private Color border;

    private Color fill;

    protected ArrayList<Shape> shapes;

    private Map<Object, Class<?>> buttonsMap;
    protected Map<String, Class<?>> classMap;

    protected Shape newShape;

    protected Shape selectedShape;

    protected Marquee marquee;

    private Point dragPos;

    private int dragFlag;

    private boolean addChange;

    private static GUI instance = null;

    public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    private GUI() {

        tabbedPane = new JTabbedPane();
        canvas = new DrawingArea();

        dragFlag = NONE;
        shapes = new ArrayList<>();
        colorChooser = new JColorChooser();
        border = Color.BLACK;

        card1 = new JPanel();
        buttonsMap = new HashMap<>();
        classMap = new HashMap<>();

        borderColor = new JButton("");
        fillColor = new JButton("");
        importShape = new JButton("");
        load = new JButton("");
        saveJson = new JButton("");
        saveJPEG = new JButton("");
        saveXml = new JButton("");
        redo = new JButton("");
        undo = new JButton("");
        delete = new JButton("");
        selectAll = new JButton("");

        importShape.setBackground(Color.lightGray);
        importShape.setBorder(null);
        img = new ImageIcon(this.getClass().getResource("/img/import.png")).getImage();
        importShape.setIcon(new ImageIcon(img));
        importShape.setToolTipText("Import Shape");
        card1.add(importShape);

        borderColor.setBackground(Color.lightGray);
        borderColor.setBorder(null);
        img = new ImageIcon(this.getClass().getResource("/img/border.png")).getImage();
        borderColor.setIcon(new ImageIcon(img));
        borderColor.setToolTipText("Border Color");
        card1.add(borderColor);

        fillColor.setBackground(Color.lightGray);
        fillColor.setBorder(null);
        img = new ImageIcon(this.getClass().getResource("/img/fill.png")).getImage();
        fillColor.setIcon(new ImageIcon(img));
        fillColor.setToolTipText("Fill Color");
        card1.add(fillColor);

        card1.setBackground(Color.LIGHT_GRAY);

        JPanel card2 = new JPanel();
        FileChooserClass action = new FileChooserClass();
        FileChooserClass.OpenL openAction = action.new OpenL();
        FileChooserClass.SaveL saveAction = action.new SaveL();
        FileChooserClass.ImportL importAction = action.new ImportL();

        borderColor.addActionListener(this);
        fillColor.addActionListener(this);
        undo.addActionListener(this);
        redo.addActionListener(this);
        delete.addActionListener(this);
        selectAll.addActionListener(this);
        importShape.addActionListener(importAction);

        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setPreviewPanel(new JPanel());

        saveJson.setBorder(null);
        saveJson.addActionListener(saveAction);
        saveJPEG.setBorder(null);
        saveJPEG.addActionListener(saveAction);
        saveXml.setBorder(null);
        saveXml.addActionListener(saveAction);
        load.setBorder(null);
        load.addActionListener(openAction);
        undo.setBorder(null);

        redo.setBorder(null);
        saveJson.setToolTipText("Save Json");
        saveJPEG.setToolTipText("Save JPEG");
        saveXml.setToolTipText("Save Xml");
        load.setToolTipText("Load");
        undo.setToolTipText("Undo");
        redo.setToolTipText("Redo");

        undo.setEnabled(false);
        redo.setEnabled(false);

        img = new ImageIcon(this.getClass().getResource("/img/saveJPEG.png")).getImage();
        saveJPEG.setIcon(new ImageIcon(img));
        saveJPEG.setBackground(Color.lightGray);
        img = new ImageIcon(this.getClass().getResource("/img/save.png")).getImage();
        saveJson.setIcon(new ImageIcon(img));
        saveJson.setBackground(Color.lightGray);
        img = new ImageIcon(this.getClass().getResource("/img/xml.png")).getImage();
        saveXml.setIcon(new ImageIcon(img));
        saveXml.setBackground(Color.lightGray);
        img = new ImageIcon(this.getClass().getResource("/img/load.png")).getImage();
        load.setIcon(new ImageIcon(img));
        load.setBackground(Color.lightGray);
        img = new ImageIcon(this.getClass().getResource("/img/undo.png")).getImage();
        undo.setIcon(new ImageIcon(img));
        undo.setBackground(Color.lightGray);
        img = new ImageIcon(this.getClass().getResource("/img/redo.png")).getImage();
        redo.setIcon(new ImageIcon(img));
        redo.setBackground(Color.lightGray);

        delete.setToolTipText("Delete");
        delete.setBorder(null);

        img = new ImageIcon(this.getClass().getResource("/img/delete.png")).getImage();
        delete.setIcon(new ImageIcon(img));
        delete.setBackground(Color.lightGray);
        img = new ImageIcon(this.getClass().getResource("/img/select.png")).getImage();
        selectAll.setIcon(new ImageIcon(img));
        selectAll.setBackground(Color.lightGray);
        selectAll.setToolTipText("Select");
        selectAll.setBorder(null);

        card2.add(saveJPEG);
        card2.add(saveJson);
        card2.add(saveXml);
        card2.add(load);
        card2.add(undo);
        card2.add(redo);
        card2.add(delete);
        card2.add(selectAll);

        card2.setBackground(Color.lightGray);

        tabbedPane.addTab("Draw", card1);
        tabbedPane.addTab("Edit", card2);

        img = new ImageIcon(this.getClass().getResource("/img/draw2.png")).getImage();
        tabbedPane.setBackground(Color.LIGHT_GRAY);
        tabbedPane.addTab("", new ImageIcon(img), card1);
        img = new ImageIcon(this.getClass().getResource("/img/edit.png")).getImage();
        tabbedPane.addTab("", new ImageIcon(img), card2);

        importNewShape("Rectangle");
        importNewShape("LineSegment");
        importNewShape("Ellipse");
        importNewShape("Circle");
        importNewShape("Triangle");
        importNewShape("Square");

        canvas.setBackground(Color.WHITE);
        canvas.setSize(800, 800);
        canvas.setVisible(true);

    }

    private void importNewShape(String name) {
        ClassLoader classLoader = GUI.class.getClassLoader();
        Class<?> shapeClass;
        try {
            shapeClass = classLoader.loadClass("eg.edu.alexu.csd.oop.paint." + name);
            importNewShape(shapeClass);
        } catch (ClassNotFoundException e) {
        }

    }

    private boolean shapeExists(String name) {
        for (Object b : buttonsMap.keySet()) {
            JButton temp = (JButton) b;
            if (temp.getToolTipText().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void importNewShape(Class<?> cls) {
        String name;

        try {
            name = cls.getName().substring(cls.getName().lastIndexOf(".") + 1, cls.getName().length());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid Class file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (shapeExists(name)) {
            JOptionPane.showMessageDialog(frame, "Shape Already Exists!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JButton newShapeButton = new JButton();
        try {
            Image img = new ImageIcon(this.getClass().getResource("/img/" + name + ".png")).getImage();
            newShapeButton.setIcon(new ImageIcon(img));
            newShapeButton.setBackground(Color.lightGray);
            newShapeButton.setBorder(null);
            newShapeButton.setToolTipText(name);
        } catch (Exception e) {
            newShapeButton = new JButton(name);
            newShapeButton.setToolTipText(name);
        }
        newShapeButton.addActionListener(this);
        buttonsMap.put(newShapeButton, cls);
        classMap.put(name, cls);
        card1.add(newShapeButton);
        card1.revalidate();
    }

    public void run() {
        frame = new JFrame("Painter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = frame.getContentPane();
        pane.add(tabbedPane, BorderLayout.NORTH);
        pane.add(canvas, BorderLayout.AFTER_LAST_LINE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
    }

    public void deselectShapes() {
        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).selected = false;
        }
        marquee = null;
        selectedShape = null;
        dragFlag = NONE;
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {

        newShape = null;
        if (marquee != null && !marquee.done) {
            marquee = null;
        }
        if (buttonsMap.containsKey(e.getSource())) {
            deselectShapes();
            try {
                newShape = (Shape) buttonsMap.get(e.getSource()).newInstance();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error Creating Shape!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == borderColor || e.getSource() == fillColor) {
            Window window = SwingUtilities.windowForComponent(borderColor);
            colorSource = e.getSource();
            JDialog dialog = new JDialog(window);
            dialog.setLocation(250, 200);
            dialog.add(colorChooser);
            dialog.pack();
            dialog.setVisible(true);
        } else if (e.getSource() == undo) {
            shapes.clear();
            deselectShapes();
            shapes = ShapesManager.getInstance().undo();
            canvas.repaint();
        } else if (e.getSource() == redo) {
            shapes.clear();
            deselectShapes();
            shapes = ShapesManager.getInstance().redo();
            canvas.repaint();
        } else if (e.getSource() == delete && marquee != null) {

            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i).selected) {
                    shapes.remove(i);
                    i--;
                }

            }
            ShapesManager.getInstance().addToHistory();
            dragFlag = NONE;
            marquee = null;
            canvas.repaint();
        } else if (e.getSource() == delete && selectedShape != null) {
            shapes.remove(selectedShape);
            dragFlag = NONE;
            ShapesManager.getInstance().addToHistory();
            canvas.repaint();
        } else if (e.getSource() == selectAll) {
            deselectShapes();
            marquee = new Marquee();
        }
    }


    private class DrawingArea extends Canvas implements MouseListener, MouseMotionListener {

        private static final long serialVersionUID = 1L;


        public DrawingArea() {
            addMouseMotionListener(this);
            addMouseListener(this);
        }


        public void paint(Graphics g) {
            for (int i = 0; i < shapes.size(); i++) {
                shapes.get(i).draw(g);
            }
            if (newShape != null) {
                newShape.draw(g);
            }
            if (marquee != null) {
                marquee.draw(g);
            }
        }


        @Override
        public void mouseDragged(MouseEvent event) {

            Point curPt = event.getPoint();

            if (marquee != null) {
                if (!marquee.done) {
                    marquee.setBottomRightCorner(curPt);
                    canvas.repaint();
                    return;
                } else if (dragFlag == MOVE_GROUP) {
                    marquee.translate(curPt.x - dragPos.x, curPt.y - dragPos.y);
                    dragPos = curPt;
                    canvas.repaint();
                    addChange = true;
                    return;
                } else if (dragFlag == RESIZE_GROUP) {
                    marquee.resizeShapes(dragPos, curPt);
                    canvas.repaint();
                    addChange = true;
                    return;
                }
            }
            if (selectedShape != null && dragFlag != NONE) {
                if (dragFlag == MOVE) {
                    selectedShape.translate(curPt.x - dragPos.x, curPt.y - dragPos.y);
                    dragPos = curPt;

                } else if (dragFlag == RESIZE) {
                    selectedShape.resize(dragPos, curPt);
                }
                addChange = true;
                canvas.repaint();
            }
        }


        @Override
        public void mouseMoved(MouseEvent event) {
            Point curPt = event.getPoint();
            if (newShape != null) {
                newShape.setBottomRightCorner(curPt);
                canvas.repaint();
            }
        }


        @Override
        public void mouseClicked(MouseEvent event) {
        }


        @Override
        public void mouseEntered(MouseEvent event) {
        }


        @Override
        public void mouseExited(MouseEvent event) {
        }


        @Override
        public void mousePressed(MouseEvent event) {

            Point curPt = event.getPoint();
            dragFlag = NONE;
            dragPos = curPt;

            if (marquee != null) {
                if (marquee.getTopLeftCorner() == null) {
                    marquee.setTopLeftCorner(curPt);
                } else if (marquee.done && !marquee.inBounds(curPt)) {
                    deselectShapes();
                } else if (marquee.done) {
                    if (marquee.getShape().contains(curPt)) {
                        dragFlag = MOVE_GROUP;
                        dragPos = curPt;
                    } else {
                        dragFlag = RESIZE_GROUP;
                        dragPos = marquee.getBottomRightCorner();
                    }
                }
                canvas.repaint();
                return;
            }
            if (selectedShape != null) {
                if (!selectedShape.inBounds(curPt)) {
                    selectedShape.selected = false;
                    selectedShape = null;
                } else {
                    dragFlag = MOVE;
                }
            }

            if (newShape != null) {

                if (newShape.getTopLeftCorner() == null) {
                    newShape.setTopLeftCorner(curPt);
                } else {
                    newShape.setBottomRightCorner(curPt);
                    shapes.add(newShape);
                    ShapesManager.getInstance().addToHistory();
                    newShape = null;
                }
            } else if (selectedShape != null && selectedShape.getAnchorForResize(curPt) != null) {
                dragFlag = RESIZE;
                dragPos = selectedShape.getAnchorForResize(curPt);
            } else {
                dragPos = curPt;
                for (int i = shapes.size() - 1; i >= 0; i--) {
                    if (shapes.get(i).intersect(curPt)) {
                        if (selectedShape != null) {
                            selectedShape.selected = false;
                        }

                        shapes.get(i).selected = true;
                        selectedShape = shapes.get(i);
                        dragFlag = MOVE;
                        break;
                    }
                }
            }
            canvas.repaint();
        }


        @Override
        public void mouseReleased(MouseEvent event) {
            if (dragFlag != NONE && addChange) {
                ShapesManager.getInstance().addToHistory();
                addChange = false;
            }
            dragFlag = NONE;

            if (marquee != null && !marquee.done) {

                Rectangle2D newArea = marquee.getArea(shapes);
                if (newArea == null) {
                    marquee = null;
                }
                canvas.repaint();
            }
        }

    }


    @Override
    public void stateChanged(ChangeEvent e) {

        if (colorSource == borderColor) {
            border = colorChooser.getColor();
        }
        if (colorSource == fillColor) {
            fill = colorChooser.getColor();
        }
        if (selectedShape != null && colorSource == borderColor) {
            border = colorChooser.getColor();
            selectedShape.setBorderColor(border);
            ShapesManager.getInstance().addToHistory();
            canvas.repaint();

        }
        if (selectedShape != null && colorSource == fillColor) {
            fill = colorChooser.getColor();
            selectedShape.setFillColor(fill);
            ShapesManager.getInstance().addToHistory();
            canvas.repaint();

        }
        if (newShape != null) {
            newShape.setBorderColor(border);
            newShape.setFillColor(fill);
        }
    }
}
