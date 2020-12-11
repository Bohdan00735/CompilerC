public class UnaryExpression extends Expression {
    Term childTerm;

    public UnaryExpression(Token thisToken, Node parentNode, KeyWords operator, Term childTerm) {
        super(thisToken, parentNode, operator);
        this.childTerm = childTerm;
    }

    public UnaryExpression(KeyWords operator, Term childTerm) {
        super(operator);
        this.childTerm = childTerm;
    }

    public UnaryExpression(Token thisToken, Node parentNode, KeyWords operator) {
        super(thisToken, parentNode, operator);
    }

    public void addChildTerm(Term childTerm) {
        this.childTerm = childTerm;
    }

    @Override
    public String generateCode() {
        return childTerm.generateCode() + "\n\r not eax;";
    }
}

