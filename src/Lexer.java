import java.io.*;
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
        text = addSpaces(text);
        String[] decomposedText = text.split("\n");
        for (int i = 0; i < decomposedText.length; i++) {
            String[] decomposedLine = decomposedText[i].split(" ");
            for (int j = 0; j < decomposedLine.length; j++) {
                if (decomposedLine[j].equals("")) continue;
                tokensText.add(determineToken(decomposedLine[j], i, j));
            }
        }
        return tokensText;
    }

    private String addSpaces(String text) {
        text = text.replace("{", " { ");
        text = text.replace("}", " } ");
        text = text.replace("(", " ( ");
        text = text.replace(")", " ) ");
        text = text.replace("!", " ! ");
        text = text.replace("+ ", " + ");
        text = text.replace("++", " ++ ");
        text = text.replace(";", " ; ");
        text = text.replace("=", " = ");
        text = text.replace("-", " - ");
        text = text.replace("||", " || ");
        text = text.replace("if", " if ");
        text = text.replace("else", " else ");
        text = text.replace(",", " , ");
        return text;
    }

    private String readFile(){
        StringBuilder text = new StringBuilder();
        try {
            InputStream inputStream = new FileInputStream(file);
            int symb;
            while ((symb = inputStream.read()) != -1) {
                text.append((char) symb);
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return text.toString();
    }

    private Token determineToken(String component, int row, int column){

        try{
            return new Token(converter.convert(component, row), component, row, column);
        }catch (MySyntaxError error){
            if (isNumeric(component)) {
                return new Token(KeyWords.NUM, component, row, column);
            }
        }return new Word(KeyWords.ID, component, row, column);
    }

    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
