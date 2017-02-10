package eg.edu.alexu.csd.oop.paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ShapesManager {


    private ArrayList<ArrayList<Shape>> history;


    int historyIndex;


    private static ShapesManager instance = null;


    private ShapesManager() {
        history = new ArrayList<>();
        history.add(new ArrayList<Shape>());
        historyIndex = 0;
    }


    public static ShapesManager getInstance() {
        if (instance == null) {
            instance = new ShapesManager();
        }
        return instance;
    }


    public ArrayList<Shape> undo() {
        if (historyIndex == 0) {
            return null;
        }
        GUI.getInstance().undo.setEnabled(historyIndex - 1 == 0 ? false : true);
        GUI.getInstance().redo.setEnabled(true);
        ArrayList<Shape> temp = new ArrayList<>();
        for (int i = 0; i < history.get(historyIndex - 1).size(); i++) {
            temp.add(history.get(historyIndex - 1).get(i).clone());
        }
        historyIndex--;

        return new ArrayList<Shape>(temp);
    }


    public ArrayList<Shape> redo() {
        if (historyIndex == history.size() - 1) {
            return null;
        }
        GUI.getInstance().undo.setEnabled(true);
        GUI.getInstance().redo.setEnabled(historyIndex + 1 == history.size() - 1 ? false : true);
        ArrayList<Shape> temp = new ArrayList<>();
        for (int i = 0; i < history.get(historyIndex + 1).size(); i++) {
            temp.add(history.get(historyIndex + 1).get(i).clone());
        }
        historyIndex++;
        return new ArrayList<Shape>(temp);
    }


    public void addToHistory() {
        while (historyIndex != history.size() - 1) {
            history.remove(history.size() - 1);
        }
        ArrayList<Shape> temp = new ArrayList<>();
        for (int i = 0; i < GUI.getInstance().shapes.size(); i++) {
            temp.add(GUI.getInstance().shapes.get(i).clone());
        }

        history.add(temp);
        historyIndex++;
        GUI.getInstance().undo.setEnabled(true);
        GUI.getInstance().redo.setEnabled(false);
    }


    public Class<?> loadClass(File source, String name) {

        try {

            String path = new File("").getAbsolutePath() + File.separatorChar + "eg" + File.separatorChar + "edu"
                    + File.separatorChar + "alexu" + File.separatorChar + "csd" + File.separatorChar + "oop"
                    + File.separatorChar + "paint" + File.separatorChar;
            new File(path).mkdirs();
            Files.copy(new File(source.toPath() + "" +  File.separatorChar + name).toPath(), new File(path + name).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            source = new File(new File("").getAbsolutePath());
            java.net.URL url = source.toURI().toURL();
            java.net.URL[] urls = new java.net.URL[]{url};

            // load this folder into Class loader
            ClassLoader cl = new URLClassLoader(urls);

            Class<?> cls = cl.loadClass("eg.edu.alexu.csd.oop.paint." + name.substring(0, name.indexOf('.')));

            try {
                File temp = new File(path + name);
                while (temp.getPath().contains("eg")) {
                    Files.delete(temp.toPath());
                    temp = temp.getParentFile();
                }
            } catch (Exception e) {
            }

            return cls;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error:\n" + e);
        }
        return null;
    }


    public void saveJson(File file) {
        // TODO Auto-generated method stub
        try {
            // File file = new File("out.json");
            FileWriter writer = new FileWriter(file);
            writer.write("{\"shapes\":[" + System.getProperty("line.separator"));
            for (int i = 0; i < GUI.getInstance().shapes.size(); i++) {

                writer.write("{\"ClassName\":" + "\"" + GUI.getInstance().shapes.get(i).getClass().toString() + "\",");
                writer.write("\"topLeftx\":" + "\" " + GUI.getInstance().shapes.get(i).topLeftCorner.x + "\",");
                writer.write("\"topLefty\":" + "\" " + GUI.getInstance().shapes.get(i).topLeftCorner.y + "\",");
                writer.write("\"bottomRightx\":" + "\" " + GUI.getInstance().shapes.get(i).bottomRightCorner.x + "\",");
                writer.write("\"bottomrighty\":" + "\" " + GUI.getInstance().shapes.get(i).bottomRightCorner.y + "\",");
                writer.write("\"borderColor\":" + "\" " + GUI.getInstance().shapes.get(i).borderColor + "\",");
                writer.write("\"fillColor\":" + "\" " + GUI.getInstance().shapes.get(i).fillColor + "\"}");
                if (i != GUI.getInstance().shapes.size() - 1) {
                    writer.write(",");
                }
                writer.write(System.getProperty("line.separator"));
            }

            writer.write("]}");
            writer.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
        }

    }


    public void loadJson(File file) {
        // TODO Auto-generated method stub

        String[] arr = new String[7];
        try {
            File readFile = file;
            Scanner in = new Scanner(readFile);
            GUI.getInstance().shapes.clear();
            GUI.getInstance().undo.setEnabled(false);
            GUI.getInstance().redo.setEnabled(false);
            GUI.getInstance().newShape = GUI.getInstance().selectedShape = GUI.getInstance().marquee = null;

            String s = in.nextLine();
            while (in.hasNext()) {
                s = in.nextLine();

                if (s.equals("]}"))
                    break;

                String data = "";
                int cnt = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        i++;
                        while (s.charAt(i) != '"') {
                            data += s.charAt(i);
                            i++;
                        }
                        arr[cnt] = data;
                        cnt++;
                        data = "";
                    }
                }

                Shape newShape;
                String className = "";
                try {
                    className = arr[0].substring(arr[0].indexOf("paint") + 6, arr[0].length());
                    newShape = (Shape) GUI.getInstance().classMap.get(className).newInstance();
                    // newShape = (Shape)
                    // Class.forName(arr[0]).getConstructor().newInstance();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Missing Shape class file: " + className + ".class\n"
                            + "Please import the shape and try again", "Error", JOptionPane.ERROR_MESSAGE);
                    GUI.getInstance().shapes.clear();
                    return;
                }

                newShape.topLeftCorner = new Point();
                newShape.bottomRightCorner = new Point();
                newShape.topLeftCorner.x = Integer.parseInt(arr[1]);
                newShape.topLeftCorner.y = Integer.parseInt(arr[2]);
                newShape.bottomRightCorner.x = Integer.parseInt(arr[3]);
                newShape.bottomRightCorner.y = Integer.parseInt(arr[4]);
                Scanner sc = new Scanner(arr[5]);
                sc.useDelimiter("\\D+");
                newShape.borderColor = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
                if (!arr[6].equals("null")) {
                    sc = new Scanner(arr[6]);
                    sc.useDelimiter("\\D+");
                    newShape.fillColor = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
                } else {
                    newShape.fillColor = null;
                }

                GUI.getInstance().shapes.add(newShape);

            }
            in.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
            GUI.getInstance().shapes.clear();

        }

        addToHistory();
        GUI.getInstance().canvas.repaint();

    }


    public void saveXML(File file) {
        // TODO Auto-generated method stub

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Shapes");
            doc.appendChild(rootElement);

            for (int i = 0; i < GUI.getInstance().shapes.size(); i++) {
                // className elements

                Element Shape = doc.createElement("Shape");
                rootElement.appendChild(Shape);

                String s = GUI.getInstance().shapes.get(i).getClass().getName().toString();
                Element className = doc.createElement("Class");
                className.appendChild(doc.createTextNode(s));
                Shape.appendChild(className);

                // TopLeftx elements
                s = Integer.toString(GUI.getInstance().shapes.get(i).topLeftCorner.x);
                Element TopLeftx = doc.createElement("TopLeftx");
                TopLeftx.appendChild(doc.createTextNode(s));
                Shape.appendChild(TopLeftx);

                // TopLefty elements
                s = Integer.toString(GUI.getInstance().shapes.get(i).topLeftCorner.y);
                Element TopLefty = doc.createElement("TopLefty");
                TopLefty.appendChild(doc.createTextNode(s));
                Shape.appendChild(TopLefty);

                // BottomRightx elements
                s = Integer.toString(GUI.getInstance().shapes.get(i).bottomRightCorner.x);
                Element BottomRightx = doc.createElement("BottomRightx");
                BottomRightx.appendChild(doc.createTextNode(s));
                Shape.appendChild(BottomRightx);

                // BottomRighty elements
                s = Integer.toString(GUI.getInstance().shapes.get(i).bottomRightCorner.y);
                Element BottomRighty = doc.createElement("BottomRighty");
                BottomRighty.appendChild(doc.createTextNode(s));
                Shape.appendChild(BottomRighty);

                // BorderColor elements
                s = (GUI.getInstance().shapes.get(i).borderColor).toString();
                Element BorderColor = doc.createElement("BorderColor");
                BorderColor.appendChild(doc.createTextNode(s));
                Shape.appendChild(BorderColor);

                // FillColor elements
                Color c = (GUI.getInstance().shapes.get(i).fillColor);
                if (c == null) {
                    Element FillColor = doc.createElement("FillColor");
                    FillColor.appendChild(doc.createTextNode("null"));
                    Shape.appendChild(FillColor);
                } else {
                    s = (GUI.getInstance().shapes.get(i).fillColor).toString();
                    Element FillColor = doc.createElement("FillColor");
                    FillColor.appendChild(doc.createTextNode(s));
                    Shape.appendChild(FillColor);
                }

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            JOptionPane.showMessageDialog(null, "Error");
        } catch (TransformerException tfe) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }


    public void loadXML(File file) {
        // TODO Auto-generated method stub

        try {

            File fXmlFile = file;

            GUI.getInstance().shapes.clear();
            GUI.getInstance().undo.setEnabled(false);
            GUI.getInstance().redo.setEnabled(false);
            GUI.getInstance().newShape = GUI.getInstance().selectedShape = GUI.getInstance().marquee = null;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Shape");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String className = eElement.getElementsByTagName("Class").item(0).getTextContent();
                    String TopLeftx = eElement.getElementsByTagName("TopLeftx").item(0).getTextContent();
                    String TopLefty = eElement.getElementsByTagName("TopLefty").item(0).getTextContent();
                    String BottomRightx = eElement.getElementsByTagName("BottomRightx").item(0).getTextContent();
                    String BottomRighty = eElement.getElementsByTagName("BottomRighty").item(0).getTextContent();
                    String BorderColor = eElement.getElementsByTagName("BorderColor").item(0).getTextContent();
                    String FillColor = eElement.getElementsByTagName("FillColor").item(0).getTextContent();

                    Shape newShape;
                    String name = "";
                    try {
                        name = className.substring(className.indexOf("paint") + 6, className.length());
                        newShape = (Shape) GUI.getInstance().classMap.get(name).newInstance();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Missing Shape class file: " + name + ".class\n"
                                + "Please import the shape and try again", "Error", JOptionPane.ERROR_MESSAGE);
                        GUI.getInstance().shapes.clear();
                        return;
                    }

                    newShape.topLeftCorner = new Point();
                    newShape.bottomRightCorner = new Point();
                    newShape.topLeftCorner.x = Integer.parseInt(TopLeftx);
                    newShape.topLeftCorner.y = Integer.parseInt(TopLefty);
                    newShape.bottomRightCorner.x = Integer.parseInt(BottomRightx);
                    newShape.bottomRightCorner.y = Integer.parseInt(BottomRighty);

                    Scanner sc = new Scanner(BorderColor);
                    sc.useDelimiter("\\D+");
                    newShape.borderColor = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
                    if (!FillColor.equals("null")) {
                        sc = new Scanner(FillColor);
                        sc.useDelimiter("\\D+");
                        newShape.fillColor = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
                    } else {
                        newShape.fillColor = null;
                    }

                    GUI.getInstance().shapes.add(newShape);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
            GUI.getInstance().shapes.clear();
        }

        addToHistory();
        GUI.getInstance().canvas.repaint();
    }

    public void saveJPEG(File file) {
        BufferedImage img = new BufferedImage(GUI.getInstance().canvas.getWidth(), GUI.getInstance().canvas.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        GUI.getInstance().canvas.print(g2d);
        g2d.dispose();

        try {
            ImageIO.write(img, "jpeg", file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

}
