

public class MySyntaxError  extends Error {
    int line;
    int column;


    public MySyntaxError(int line, int column, String message) {
        super(message);
        this.line = line;
        this.column = column;
    }
}
