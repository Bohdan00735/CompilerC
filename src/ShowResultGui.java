import javax.swing.*;

public class ShowResultGui {
    public ShowResultGui(String header, String text) {
        final JFrame theFrame = new JFrame();
        theFrame.setTitle(header);
        theFrame.setSize(500, 500);
        theFrame.setLocation(550, 400);

        JPanel mainPanel = new JPanel();

        JTextArea theText = new JTextArea(5,25); //create the text area

        theText.append(text);
        mainPanel.add(theText); //add the text area to the panel

        theFrame.getContentPane().add(mainPanel); //add the panel to the frame
        theFrame.pack();
        theFrame.setVisible(true);
    }
}
