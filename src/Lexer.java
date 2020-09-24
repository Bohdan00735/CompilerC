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

    public ArrayList<Token> decomposeTextOnTokens(String text){
        ArrayList<Token> tokensText = new ArrayList<>();
        String[] decomposedText = text.split("\n\r");
        for (int i = 0; i < decomposedText.length; i++) {
            String[] decomposedLine = decomposedText[i].split(" ");
            for (int j = 0; j < decomposedLine.length; j++) {
                tokensText.add(determineToken(decomposedLine[i], i));
            }
        }
        return tokensText;
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
