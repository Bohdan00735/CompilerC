public class Term extends  Node {
    Expression expression;
    Num num;
    public Term(Token thisToken, Node parentNode) {
        super(thisToken, parentNode);
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Term(Num num) {
        this.num = num;
    }

    public Term() {
    }

    public Term(Token thisToken) {
        super(thisToken);
    }
}
