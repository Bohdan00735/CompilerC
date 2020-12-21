public class Main {
    public static void main(String[] args) {
        /*FileChooser fileChooser = new FileChooser();

        try{
            while(fileChooser.filePath == null){
                 Thread.sleep(5000);
            }*/
            Lexer lexer = new Lexer("./resources/1-17-java-IO-81-Melniichuk.c");
            Parser parser = new Parser(lexer.decomposeTextOnTokens());

            try{
                CodeGenerator codeGenerator = new CodeGenerator(parser.parse(),parser.functionsAst, parser.elements);
                codeGenerator.generateCode();
                codeGenerator.createFile("1-17-java-IO-81-Melniichuk.asm");
            }catch (MySyntaxError error){
                System.out.printf(error.getMessage()+" in (%d;%d)", error.line, error.column);
            }

        /*}catch (MySyntaxError | InterruptedException error){
            System.out.println(error.getMessage());
        }*/

    }
}
