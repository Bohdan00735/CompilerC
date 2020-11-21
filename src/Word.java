public class Word extends Token {
    String description;

    public Word(KeyWords type, String marking, int line, int column) {
        super(type, marking, line, column);
    }
}
