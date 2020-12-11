import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;

public class FileChooser extends JFrame {
    private  JFileChooser fileChooser;
    private JButton btnChooseFile;
    private JButton btnChooseDefault;
    private JButton btnReadDefault;
    public String filePath;
    private String DEFAULT_FILE_PATH  = "./resources/1-17-java-IO-81-Melniichuk.c";
    JTextArea defaultFileText;
    InputStream inputStream;

    public FileChooser() throws HeadlessException {



        btnChooseFile = new JButton("Open *.c file from computer");
        btnChooseDefault = new JButton("Compile default file");
        btnReadDefault = new JButton("open default file");
        defaultFileText = new JTextArea();

        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose .c file which you want to compile");

        addListeners();

        JPanel contents = new JPanel();
        contents.add(btnChooseFile);
        contents.add(btnChooseDefault);
        contents.add(btnReadDefault);
        contents.add(defaultFileText);
        setContentPane(contents);
        setSize(500,400);
        setVisible(true);

    }

    private void addListeners() {
        btnChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Choose file");
                fileChooser.setFileFilter(new FileNameExtensionFilter("c files", "c"));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(FileChooser.this);
                if (result == JFileChooser.APPROVE_OPTION )
                    filePath = fileChooser.getSelectedFile().getPath();


                FileChooser.this.dispose();
            }
        });

        btnChooseDefault.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filePath = DEFAULT_FILE_PATH;
                FileChooser.this.dispose();
            }
        });

        btnReadDefault.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultFileText.append("int main(){\n" +
                        "return !135+6;\n" +
                        "}\n");

            }
        });
    }


    public String getPath() {
        return filePath;
    }
}
