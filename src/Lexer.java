import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Lexer {
    String file;
    ArrayList<Token> tokens;
    Converter converter;

    public Lexer(String file) {
        this.file = file;
        tokens = new ArrayList<>();
        converter = new Converter();
    }

    public ArrayList<Token> decomposeTextOnTokens(){
        String text = readFile();
        ArrayList<Token> tokensText = new ArrayList<>();
        addSpaces(text);
        String[] decomposedText = text.split("\r\n");
        for (int i = 0; i < decomposedText.length; i++) {
            String[] decomposedLine = decomposedText[i].split(" ");
            for (int j = 0; j < decomposedLine.length; j++) {
                tokensText.add(determineToken(decomposedLine[j], i, j));
            }
        }
        return tokensText;
    }

    private void addSpaces(String text) {
        text.replace("{", " { ");
        text.replace("}", " } ");
        text.replace("(", " ( ");
        text.replace(")", " ) ");
        text.replace("!", " ! ");
        text.replace("+", " + ");
        text.replace(";", " ; ");

    }

    private String readFile(){
        String text = "";
        try (FileReader reader = new FileReader(file)) {
            int symb;
            while ((symb = reader.read()) != -1) {
                text += (char) symb;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return text;
    }

    private Token determineToken(String component, int row, int column){

        try{
            return new Token(converter.convert(component, row), component, row, column);
        }catch (MySyntaxError error){
            if (isNumeric(component)) {
                return new Num(KeyWords.NUM, component, row, column);
            }
        }return new Word(KeyWords.ID, component, row, column);
    }

    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
