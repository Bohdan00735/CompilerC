public class Token {
    public KeyWords type;
    public String marking;
    public int line;
    public int column;

    public Token(KeyWords type, String marking, int line, int column) {
        this.type = type;
        this.marking = marking;
        this.line = line;
        this.column = column;
    }

    public Token(KeyWords type, String marking) {
        this.type = type;
        this.marking = marking;
    }

    public Token(KeyWords type, int line, int column) {
        this.type = type;
        this.line = line;
        this.column = column;
    }
}
