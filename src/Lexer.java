import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Lexer {
    String file;
    ArrayList<Token> tokens;
    Konverter konverter;

    public Lexer(String file) {
        this.file = file;
        tokens = new ArrayList<>();
        konverter = new Konverter();
    }

    public ArrayList<Token> decomposeTextOnTokens(){
        String text = readFile();
        ArrayList<Token> tokensText = new ArrayList<>();
        String[] decomposedText = text.split("\r\n");
        for (int i = 0; i < decomposedText.length; i++) {
            String[] decomposedLine = decomposedText[i].split(" ");
            for (int j = 0; j < decomposedLine.length; j++) {
                tokensText.add(determineToken(decomposedLine[j], i));
            }
        }
        return tokensText;
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

    private Token determineToken(String component, int row){

        try{
            return new Token(konverter.konvert(component), component, row);
        }catch (SyntaxError error){
            if (isNumeric(component)) {
                return new Num(KeyWords.NUM, component, row);
            }
        }return new Word(KeyWords.ID, component, row);
    }

    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
