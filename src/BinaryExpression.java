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
        String result = leftOperand.generateCode() +
                rightOperand.generateCode() +
                "\n\r pop ebx;\n" +
                "pop eax;\n\r";
        switch (super.getOperator()){
            case PLUS:
                result+="add eax, ebx;";
                break;
                // will be added other binary operators
        }
        result+="\n\r push eax; \n\r";
        return result;
    }
}