public class BinaryExpression extends Expression {
    Term leftOperand;
    Term rightOperand;
    private int pointersCounter = 0;

    public BinaryExpression(Term leftOperand, KeyWords operator, Term rightOperand, int pointersCounter) {
        super(operator);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.pointersCounter = pointersCounter;
    }

    @Override
    public String generateCode() {
        if (super.getOperator() == KeyWords.OR || super.getOperator() == KeyWords.LESS_EQUALS){
            return generateForLogicOperation();
        }
        String result = leftOperand.generateCode() +
                rightOperand.generateCode() +
                "\n pop ebx;\n" +
                "pop eax;\n";
        switch (super.getOperator()){
            case PLUS:
                result+="add eax, ebx;";
                break;
            case MINUS:
                result+="sub eax, ebx";
                break;
            case DIVISION:
                result+="xor edx, edx\n" +
                        "div ebx";

        }
        result+="\n\r push eax; \n\r";
        return result;
    }

    private String generateForLogicOperation() {
        String result = leftOperand.generateCode();
        switch (super.getOperator()){
            case OR:
                result+="\npop eax;\n" +
                        "cmp eax,0;\n" +
                        "je _nextCalculation"+ pointersCounter +"\n"+
                        "push 1\n"+"jmp _endOperation"+pointersCounter+"\n"+
                        "_nextCalculation"+ pointersCounter +":\n"+
                rightOperand.generateCode()+"\npop eax; \n cmp eax,0;" +
                        "\n xor eax, eax\n" +
                        "setne al;\n" +
                        "push eax;\n" +
                        "_endOperation"+pointersCounter+":\n";
            case LESS_EQUALS:
                result+=rightOperand.generateCode()+
                        "\n pop ebx;\n" +
                        "pop eax;\n" +
                        "cmp eax, ebx\n" +
                        "jle _returnTrue"+pointersCounter+
                        "\n push 1\n" +
                        "jmp _continueStatment"+pointersCounter+
                        "\n_returnTrue"+pointersCounter+":\n" +
                        "push 0\n" +
                        "_continueStatment"+pointersCounter+":\n";
        }
        pointersCounter++;
        return result;
    }
}