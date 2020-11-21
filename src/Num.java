public class Num extends Token {
    KeyWords format;
    String name;

    public Num(KeyWords type, String value, int line, int column) {
        super(type, value, line, column);

    }

    public Num(KeyWords type, int line,int column, KeyWords format, String name) {
        super(type, line, column);
        this.format = format;
        this.name = name;
    }

    public Num(KeyWords type, String marking, int line, int column, KeyWords format) {
        super(type, marking, line, column);
        this.format = format;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(KeyWords format) {
        this.format = format;
    }
}
