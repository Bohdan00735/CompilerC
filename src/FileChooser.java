import javax.swing.*;
import java.awt.*;

public class FileChooser extends JFrame {
    private  JFileChooser fileChooser;

    public FileChooser() throws HeadlessException {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose .c file which you want to compile");

    }
}
