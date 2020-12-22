import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class  CodeGenerator {
    private AST ast;
    private String asmCode;
    private HashMap<String, AST> functionsAst;
    private HashMap<String, Assign> elements;


    private String masmTemplate =".586\n" +
            ".model flat, stdcall\n" +
            "option casemap :none ;to distinguish between uppercase and lowercase letters\n" +
            ".stack 4096\n" +
            "include \\masm32\\include\\kernel32.inc\n" +
            "include \\masm32\\include\\user32.inc \n" +
            "includelib \\masm32\\lib\\kernel32.lib\n" +
            "includelib \\masm32\\lib\\user32.lib\n" +
            "include module.inc;";

    public CodeGenerator(AST ast, HashMap<String, AST> functionsAst, HashMap<String, Assign> elements) {
        this.ast = ast;
        this.functionsAst = functionsAst;
        this.elements = elements;
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
        String result = "\n.data\n" +
                "Header db \" Result \" ,0" +
                "\n textBuf dd 45 dup(?)\n" +
                "result dd 0";

        for (Assign as:elements.values()
             ) {
            result+=as.generateInitialisation();
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
        return "2-17-java-IO-81-Melniichuk.asm";
    }
}
