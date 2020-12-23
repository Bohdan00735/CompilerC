import java.util.ArrayList;
import java.util.Map;

public class Function extends Compound {
    AST childAst;
    KeyWords returnType;
    Map<KeyWords, String> inputParam;
    String name;

    public Function(Token thisToken, Node parentNode,int depth, KeyWords returnType, String name, Map<KeyWords, String> inputParam) {
        super(thisToken, parentNode, depth);
        this.returnType = returnType;
        this.name = name;
        this.inputParam = inputParam;
    }

    public void setChildAst(AST childAst) {
        this.childAst = childAst;
    }
}
