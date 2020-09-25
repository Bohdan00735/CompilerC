import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class  CodeGenerator {
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

    public boolean createFile(String fileName){
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            writer.write(asmCode);
            writer.flush();
            return true;
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
