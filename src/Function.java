import java.util.ArrayList;
import java.util.Map;

public class Function extends Node {
    AST childAst;
    KeyWords returnType;
    Map<KeyWords, String> inputParam;
    String name;

    public Function(Token thisToken, Node parentNode, KeyWords returnType, String name, Map<KeyWords, String> inputParam) {
        super(thisToken, parentNode);
        this.returnType = returnType;
        this.name = name;
        this.inputParam = inputParam;
    }

    public void setChildAst(AST childAst) {
        this.childAst = childAst;
    }
}
