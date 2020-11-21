import jdk.internal.org.jline.reader.SyntaxError;

public class MySyntaxError extends SyntaxError {
    String text;

    public MySyntaxError(int line, int column, String message) {
        super(line, column, message);
    }
}
