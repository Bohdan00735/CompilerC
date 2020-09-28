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
        asmCode="";
        asmCode+= masmTemplate;
        if (ast == null){
            throw new SyntaxError("main not found");
        }
        if (functionsAst.size() > 0){
            //analise and create functions from functionsAst
        }
        else {
            asmCode += "\n .code \n";
            //build main proc
            asmCode+=configMain();
            asmCode+= "end main";
        }
    }

    private  String configMain(){
        String function = "main: \n";
        for (Node child:ast.getRoot().getChildNodes()
             ) {
            if(child.getToken().type == KeyWords.RETURN){
                Num num = (Num) child.getToken();
                if (num.format == KeyWords.INT){function += String.format("mov ebx, %s\nret\n", num.marking);
                }else {//get float
                    int intBits = Float.floatToIntBits(Float.parseFloat(num.marking));
                    String binary = Integer.toBinaryString(intBits)+"b";
                    function += String.format("mov ebx, %s\nret\n", binary);
                }
                break;
            }
        }
        return function;
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
