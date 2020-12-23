import java.util.Hashtable;

public class Converter {
    Hashtable< String, KeyWords> keyWordsStringHashtable = new Hashtable<>();

    public Converter() {
        keyWordsStringHashtable.put("int", KeyWords.INT);
        keyWordsStringHashtable.put( "main", KeyWords.MAIN);
        keyWordsStringHashtable.put( "(", KeyWords.LPAR);
        keyWordsStringHashtable.put( ")", KeyWords.RPAR);
        keyWordsStringHashtable.put( "{", KeyWords.LCBRAC);
        keyWordsStringHashtable.put("}", KeyWords.RCBRAC);
        keyWordsStringHashtable.put("return", KeyWords.RETURN);
        keyWordsStringHashtable.put( "num", KeyWords.NUM);
        keyWordsStringHashtable.put("char", KeyWords.CHAR);
        keyWordsStringHashtable.put( "float", KeyWords.FLOAT);
        keyWordsStringHashtable.put( ";", KeyWords.SEMICOLON);
        keyWordsStringHashtable.put("!", KeyWords.EXCLAMATION_POINT);
        keyWordsStringHashtable.put("+", KeyWords.PLUS);
        keyWordsStringHashtable.put("=", KeyWords.EQUALS);
        keyWordsStringHashtable.put("||",KeyWords.OR);
        keyWordsStringHashtable.put("-",KeyWords.MINUS);
        keyWordsStringHashtable.put("if",KeyWords.IF);
        keyWordsStringHashtable.put("else", KeyWords.ELSE);

    }

    public KeyWords convert(String word, int row){
        KeyWords keyWord = keyWordsStringHashtable.get(word);
        if (keyWord == null){throw new MySyntaxError(row, 0, String.format("No such keyword as %s", word));}
        return keyWord;
    }
}
