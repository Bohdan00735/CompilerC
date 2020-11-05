import java.util.Hashtable;

public class Konverter {
    Hashtable< String, KeyWords> keyWordsStringHashtable = new Hashtable<>();

    public Konverter() {
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
        keyWordsStringHashtable.put( ":", KeyWords.SEMICOLON);
        keyWordsStringHashtable.put("!", KeyWords.EXCLAMATION_POINT);
        keyWordsStringHashtable.put("+", KeyWords.PLUS);
    }

    public KeyWords konvert(String word){
        KeyWords keyWord = keyWordsStringHashtable.get(word);
        if (keyWord == null){throw new SyntaxError(String.format("No such keyword as %s", word));}
        return keyWord;
    }
}
