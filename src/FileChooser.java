import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    JTextArea readFileText;
    InputStream inputStream;

    public FileChooser() throws HeadlessException {
        setTitle("Compiler For C by Melniichuk Bohdan");

        btnChooseFile = new JButton("Open *.c file from computer");
        btnChooseDefault = new JButton("Compile default file");
        btnReadDefault = new JButton("open default file");
        defaultFileText = new JTextArea(60,40);
        readFileText = new JTextArea(1,30);
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose .c file which you want to compile");
        JScrollPane scrollPane = new JScrollPane(defaultFileText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        addListeners();

        JPanel contents = new JPanel();

        contents.add(btnChooseFile);
        /*contents.add(btnChooseDefault);
        contents.add(btnReadDefault);*/
        contents.add(readFileText);

        contents.add(scrollPane);
        setLayout(new FlowLayout());
        setContentPane(contents);
        setLocationRelativeTo(null);
        setSize(500,1000);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    public void showResult(String result){
        defaultFileText.append(result);

    }

    private void addListeners() {
        btnChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Choose file");
                fileChooser.setFileFilter(new FileNameExtensionFilter("c files", "c"));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(FileChooser.this);
                if (result == JFileChooser.APPROVE_OPTION ){
                    filePath = fileChooser.getSelectedFile().getPath();
                    readFileText.append("chosen file: "+ filePath);
                }

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
