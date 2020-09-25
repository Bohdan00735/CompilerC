import java.util.HashMap;

public class CodeGenerator {
    AST ast;
    private String asmCode;
    HashMap<String, AST> functionsAst;

    private String masmTemplate ="";

    public CodeGenerator(AST ast, HashMap<String, AST> functionsAst) {
        this.ast = ast;
        this.functionsAst = functionsAst;
    }

    public void generateCode(){
        if (functionsAst != null){
            //analise and create functions from functionsAst
        }
        else {
            //build main proc
        }
    }

    public String configMain(){
        
        return null;
    }

    public String writeFunc(){
        //TODO
        return null;
    }
}
