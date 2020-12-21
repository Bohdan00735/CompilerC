public class ReturnNode extends Node{
    Function parentNode;

    public ReturnNode(Token thisToken, Function parentNode) {
        super(thisToken);
        this.parentNode = parentNode;
    }

    @Override
    public String generateCode() {
        StringBuilder res = new StringBuilder();
        for (Node child:super.getChildNodes()
        ) {
            res.append(child.generateCode());
        }
        res.append("\npop result\n");
        switch (parentNode.returnType){
            case FLOAT:
                res.append("Push Offset textBuf\n" +
                    "Push result\n" +
                    "call FloatToDec32\n" +
                    "invoke MessageBoxA, 0, ADDR textBuf, ADDR Header, 0\n");
            break;
            case INT:
                res.append("push offset textBuf\n" +
                        "  push offset result\n" +
                        "  push 128\n" +
                        "  call HexToDec\n" +
                        "  invoke MessageBoxA, 0, ADDR textBuf, ADDR Header, 0\n");
                break;
        }
        return res.append("ret\n").toString();
    }

}
