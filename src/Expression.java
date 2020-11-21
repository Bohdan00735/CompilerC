public class Expression extends Term {
    private KeyWords operator;

    public Expression(Token thisToken, Node parentNode, KeyWords operator) {
        super(thisToken, parentNode);
        this.operator = operator;
    }

    public Expression(KeyWords operator) {
        this.operator = operator;
    }

    public Expression(Token thisToken, Node parentNode) {
        super(thisToken, parentNode);
    }

    public String formAction(){
        return  null;
    }

}
