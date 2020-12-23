import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class  CodeGenerator {
    private AST ast;
    private String asmCode;
    private HashMap<String, AST> functionsAst;
    ArrayList<HashMap<String, Assign>> encloseVariables;



    private String masmTemplate =".586\n" +
            ".model flat, stdcall\n" +
            "option casemap :none ;to distinguish between uppercase and lowercase letters\n" +
            ".stack 4096\n" +
            "include \\masm32\\include\\kernel32.inc\n" +
            "include \\masm32\\include\\user32.inc \n" +
            "includelib \\masm32\\lib\\kernel32.lib\n" +
            "includelib \\masm32\\lib\\user32.lib\n" +
            "include module.inc;";

    public CodeGenerator(AST ast, HashMap<String, AST> functionsAst, ArrayList<HashMap<String, Assign>> encloseVariables) {
        this.ast = ast;
        this.functionsAst = functionsAst;
        this.encloseVariables = encloseVariables;
    }

    public void generateCode(){
        asmCode = masmTemplate;
        asmCode += configData();

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
        StringBuilder result = new StringBuilder("\n.data\n" +
                "Header db \" Result \" ,0" +
                "\n textBuf dd 45 dup(?)\n" +
                "result dd 0 dup(0)\n");
        HashSet<String> names = makeSetOfVariables();
        for (String name:names
             ) {
            result.append(name).append(" dd 0 dup(0)\n");
        }
        return result.toString();
    }

    private HashSet<String> makeSetOfVariables() {
        HashSet<String> result = new HashSet<>();
        for (HashMap<String, Assign> map:encloseVariables
             ) {
            for (Assign assign:map.values()
                 ) {
                result.add(assign.name);
            }
        }
        return result;
    }


    private  String configMain(){
        StringBuilder function = new StringBuilder("main: \n");
        for (Node child:ast.getRoot().getChildNodes()
             ) {
            function.append(child.generateCode());
        }
        function.append("Invoke ExitProcess, 0");
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

    public String createPass(String filePath) {
        String[] tokens = filePath.split("/");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length-1; i++) {
            result.append(tokens[i]+"/");
        }
        result.append("2-17-java-IO-81-Melniichuk.asm").toString();
        return "4-17-java-IO-81-Melniichuk.asm";
    }
}
