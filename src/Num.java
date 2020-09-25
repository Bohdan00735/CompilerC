public class Num extends Token {
    KeyWords format;
    String name;

    public Num(KeyWords type, String value, int line) {
        super(type, value, line);

    }

    public Num(KeyWords type, String marking, int line, KeyWords format) {
        super(type, marking, line);
        this.format = format;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(KeyWords format) {
        this.format = format;
    }
}
