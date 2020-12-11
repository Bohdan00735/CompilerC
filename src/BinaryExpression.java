import java.util.ArrayList;

public class BinaryExpression extends Expression {
    Term leftOperand;
    Term rightOperand;

    public BinaryExpression(Term leftOperand, KeyWords operator, Term rightOperand) {
        super(operator);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public String generateCode() {
        return leftOperand.generateCode() +
                "\n\r mov ebx, eax; \n\r" +
                rightOperand.generateCode() +
                "\n\r add eax,ebx \n\r";
    }
}