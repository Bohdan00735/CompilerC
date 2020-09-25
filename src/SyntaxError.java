public class SyntaxError extends Error {
    String text;

    public SyntaxError(String text) {
        this.text = text;
    }
}
