public class Main {
    public static void main(String[] args) {
        try{Lexer lexer = new Lexer("D:\\Bohdan\\CompilerC\\files\\1-17-java-IO-81-Melniichuk");
            Parser parser = new Parser(lexer.decomposeTextOnTokens());
            CodeGenerator codeGenerator = new CodeGenerator(parser.parse(),parser.functionsAst);
            codeGenerator.generateCode();
            codeGenerator.createFile("1-17-java-IO-81-Melniichuk.asm");
        }catch (SyntaxError error){
            System.out.println(error.text);
        }

    }
}
