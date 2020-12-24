import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FileChooser fileChooser = new FileChooser();
        final JPanel panel = new JPanel();

        try{
            while(fileChooser.filePath == null){
                 Thread.sleep(5000);
            }
            Lexer lexer = new Lexer(fileChooser.filePath);
            Parser parser = new Parser(lexer.decomposeTextOnTokens());

            try{
                CodeGenerator codeGenerator = new CodeGenerator(parser.parse());
                codeGenerator.generateCode();

                codeGenerator.createFile(codeGenerator.createPass(fileChooser.filePath));
            }catch (MySyntaxError error){
                JOptionPane.showMessageDialog(panel,
                        String.format(error.getMessage()+" in (%d;%d)", error.line, error.column),
                        "Error", JOptionPane.ERROR_MESSAGE);

            }

        }catch (MySyntaxError | InterruptedException error){
            JOptionPane.showMessageDialog(panel, error.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
