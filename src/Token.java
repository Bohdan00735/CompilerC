public class Token {
    public KeyWords type;
    public String marking;
    public int line;

    public Token(KeyWords type, String marking, int line) {
        this.type = type;
        this.marking = marking;
        this.line = line;
    }
}
