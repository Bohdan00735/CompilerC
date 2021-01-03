import java.io.FileWriter;
import java.io.IOException;

public class  CodeGenerator {
    private AST ast;
    private String asmCode;
    String path;

    private String masmTemplate =".586\n" +
            ".model flat, stdcall\n" +
            "option casemap :none ;to distinguish between uppercase and lowercase letters\n" +
            ".stack 4096\n" +
            "include \\masm32\\include\\kernel32.inc\n" +
            "include \\masm32\\include\\user32.inc \n" +
            "includelib \\masm32\\lib\\kernel32.lib\n" +
            "includelib \\masm32\\lib\\user32.lib\n" +
            "include module.inc;";

    public CodeGenerator(AST ast, String path) {
        this.ast = ast;
        this.path = path;
    }

    public String getAsmCode() {
        return asmCode;
    }

    public void generateCode(){
        asmCode = masmTemplate;
        asmCode += configData();

        if (ast == null){
            throw new MySyntaxError(0,0, "functions not found");
        }
        else {

            asmCode += "\n .code \n";
            //build main proc
            asmCode+= ast.generateCode();
            asmCode+= "\n\r end main";
        }
    }

    private String configData() {
        return "\n.data\n" +
                "Header db \" Result \" ,0" +
                "\n textBuf dd 45 dup(?)\n" +
                "result dd 0 dup(0)\n";
    }

    public void createFile(String fileName){
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            writer.write(asmCode);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public String createPass(String filePath) {
        String[] tokens = filePath.split("/");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length-1; i++) {
            result.append(tokens[i]+"/");
        }
        result.append("2-17-java-IO-81-Melniichuk.asm").toString();
        return "RGR-17-java-IO-81-Melniichuk.asm";
    }
}
