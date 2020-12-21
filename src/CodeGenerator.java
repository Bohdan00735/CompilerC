import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class  CodeGenerator {
    private AST ast;
    private String asmCode;
    private HashMap<String, AST> functionsAst;

    String masmTemplate =".586\n" +
            ".model flat, stdcall\n" +
            "option casemap :none ;to distinguish between uppercase and lowercase letters\n" +
            "include \\masm32\\include\\windows.inc\n" +
            "include D:\\masm32\\include\\kernel32.inc\n" +
            "include D:\\masm32\\include\\user32.inc\n" +
            "includelib D:\\masm32\\lib\\kernel32.lib\n" +
            "includelib D:\\masm32\\lib\\user32.lib\n";

    public CodeGenerator(AST ast, HashMap<String, AST> functionsAst) {
        this.ast = ast;
        this.functionsAst = functionsAst;
    }

    public void generateCode(){
        asmCode = configData();

        if (ast == null){
            throw new MySyntaxError(0,0, "main not found");
        }
        if (functionsAst.size() > 0){
            //analise and create functions from functionsAst
        }
        else {

            asmCode += "\n .code \n";
            //build main proc
            asmCode+=configMain();
            asmCode+= "\n\r end main";
        }
    }

    private String configData() {
        String result = ".data";

        return result;
    }


    private  String configMain(){
        StringBuilder function = new StringBuilder("main: \n");
        for (Node child:ast.getRoot().getChildNodes()
             ) {
            function.append(child.generateCode());
        }
        return function.toString();
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
