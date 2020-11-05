public class Expression extends Node {
    private KeyWords operator;

    public Expression(Token thisToken, Node parentNode, KeyWords operator) {
        super(thisToken, parentNode);
        this.operator = operator;
    }

    public void addTerm(Num num){};
}
