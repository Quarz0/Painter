package eg.edu.alexu.csd.oop.paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooserClass extends JFrame {

    private File f;

    private JTextField filename = new JTextField(), dir = new JTextField();

    public class OpenL implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON & XML files (*.json; *.xml)", "json", "xml");
            c.setFileFilter(filter);
            c.setAcceptAllFileFilterUsed(false);
            ShapesManager obj = ShapesManager.getInstance();

            int rVal = c.showOpenDialog(FileChooserClass.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName());
                dir.setText(c.getCurrentDirectory().toString());
                String name = dir.getText() + File.separatorChar + filename.getText();

                String s = name.substring(name.lastIndexOf(".") + 1, name.length());

                if (s.equals("xml")) {
                    f = new File(name);
                    if (!f.exists()) {
                        JOptionPane.showMessageDialog(null, "Error");
                        return;
                    }
                    obj.loadXML(f);
                } else if (s.equals("json")) {
                    f = new File(name);
                    if (!f.exists()) {
                        JOptionPane.showMessageDialog(null, "Error");
                        return;
                    }
                    obj.loadJson(f);
                } else {
                    JOptionPane.showMessageDialog(null, "Error");
                    return;
                }

            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                dir.setText(null);
            }
        }
    }

    class SaveL implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
            // Demonstrate "Save" dialog:

            FileNameExtensionFilter filter = null;

            if (e.getSource() == GUI.getInstance().saveXml) {
                filter = new FileNameExtensionFilter("XML (*.xml)", "xml");
            } else if (e.getSource() == GUI.getInstance().saveJson) {
                filter = new FileNameExtensionFilter("JSON (*.json)", "json");
            } else if (e.getSource() == GUI.getInstance().saveJPEG) {
                filter = new FileNameExtensionFilter("JPEG (*.jpeg)", "jpeg");
            }

            c.setFileFilter(filter);
            c.setAcceptAllFileFilterUsed(false);

            ShapesManager obj = ShapesManager.getInstance();

            int rVal = c.showSaveDialog(FileChooserClass.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName());
                dir.setText(c.getCurrentDirectory().toString());
                String name = dir.getText() + File.separatorChar + filename.getText();

                String s = name.substring(name.lastIndexOf(".") + 1, name.length());
                f = new File(name);
                if (e.getSource() == GUI.getInstance().saveJPEG) {
                    if (!name.contains(".")) {
                        f = new File(name + ".jpeg");

                    } else if (!s.equals("jpeg")) {
                        JOptionPane.showMessageDialog(null, "Error");
                        return;
                    }
                    obj.saveJPEG(f);
                } else if (e.getSource() == GUI.getInstance().saveXml) {
                    if (!name.contains(".")) {
                        f = new File(name + ".xml");

                    } else if (!s.equals("xml")) {
                        JOptionPane.showMessageDialog(null, "Error");
                        return;
                    }
                    obj.saveXML(f);
                } else if (e.getSource() == GUI.getInstance().saveJson) {
                    if (!name.contains(".")) {
                        f = new File(name + ".json");
                    } else if (!s.equals("json")) {
                        JOptionPane.showMessageDialog(null, "Error");
                        return;
                    }
                    obj.saveJson(f);
                }

            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                dir.setText(null);
            }
        }
    }

    class ImportL implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
            // Demonstrate "Open" dialog:

            FileNameExtensionFilter filter = new FileNameExtensionFilter("CLASS (*.class)", "class");
            c.setFileFilter(filter);
            c.setAcceptAllFileFilterUsed(false);
            ShapesManager obj = ShapesManager.getInstance();

            int rVal = c.showOpenDialog(FileChooserClass.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName());
                dir.setText(c.getCurrentDirectory().toString());
                GUI.getInstance().importNewShape(obj.loadClass((new File(dir.getText() + File.separatorChar)), filename.getText()));
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                dir.setText(null);
            }
        }
    }
}
