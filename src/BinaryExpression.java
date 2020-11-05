import java.util.ArrayList;

public class BinaryExpression extends Expression {
    Expression[] expressionsTerms;
    ArrayList<Num> constTerms;

    public BinaryExpression(Token thisToken, Node parentNode, KeyWords operator, Expression[] expressionsTerms) {
        super(thisToken, parentNode, operator);
        this.expressionsTerms = expressionsTerms;
    }

    public BinaryExpression(Token thisToken, Node parentNode, KeyWords operator, ArrayList<Num> constTerms) {
        super(thisToken, parentNode, operator);
        this.constTerms = constTerms;
    }

    public BinaryExpression(Token thisToken, Node parentNode, KeyWords operator, Expression expression,Num constant ) {
        super(thisToken, parentNode, operator);
        expressionsTerms = new Expression[]{expression};
        constTerms = new ArrayList<Num>();
        constTerms.add(constant);
    }

    @Override
    public void addTerm(Num num) {
        constTerms.add(num);
    }
}