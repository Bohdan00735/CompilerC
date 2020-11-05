public class UnaryExpression extends Expression {
    Expression childExpression;
    Num constant;

    public UnaryExpression(Token thisToken, Node parentNode, KeyWords operator, Expression childExpression) {
        super(thisToken, parentNode, operator);
        this.childExpression = childExpression;
    }

    public UnaryExpression(Token thisToken, Node parentNode, KeyWords operator, Num constant) {
        super(thisToken, parentNode, operator);
        this.constant = constant;
    }

    public UnaryExpression(Token thisToken, Node parentNode, KeyWords operator) {
        super(thisToken, parentNode, operator);
    }

    public void setChildExpression(Expression childExpression) {
        this.childExpression = childExpression;
    }

    @Override
    public void addTerm(Num num) {
        this.constant = num;
    }
}

