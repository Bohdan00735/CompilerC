public class Conditional extends Node{
    Term expressionToComparison;
    Compound ifPart;
    Compound elsePart;
    int pointing;

    public Conditional(Token thisToken, Node parentNode, Term expressionToComparison, int pointing) {
        super(thisToken, parentNode);
        this.expressionToComparison = expressionToComparison;
        this.pointing = pointing;
    }

    public void setIfPart(Compound ifPart) {
        this.ifPart = ifPart;
    }

    public void setElsePart(Compound elsePart) {
        this.elsePart = elsePart;
    }

    @Override
    public String generateCode() {
        String result = expressionToComparison.generateCode()+
                "\n pop eax\n" +
                "cmp eax, 0\n" +
                "je else"+pointing+"\n"+ifPart.generateCode()+
                "\nelse"+pointing+":\n";
        if (elsePart != null) result+= elsePart.generateCode();
        return result;
    }
}
