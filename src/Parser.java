import javax.print.DocFlavor;
import java.lang.reflect.Parameter;
import java.security.Key;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

public class Parser {
    ArrayList<Token> tokens;
    AST mainAst;
    HashMap<String, AST> functionsAst;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public AST parse(){
        functionsAst = new HashMap<>();
        for (Iterator<Token> tokenIterator = tokens.iterator(); tokenIterator.hasNext();){
            Token current = tokenIterator.next();
            if (current.type == KeyWords.INT || current.type == KeyWords.FLOAT){
                Token next = tokenIterator.next();
                switch (next.type){
                    case MAIN:
                        mainAst = analiseFunction(current.type, null, next, tokenIterator);
                        continue;

                    case LINE:
                        functionsAst.put(current.marking, analiseFunction(current.type, mainAst.getRoot(), next, tokenIterator));
                        continue;

                        //TODO for param
                    default: throw new SyntaxError(String.format("Error syntax after type in raw %d",next.line));
                }

            }
            else if(current.type == KeyWords.RETURN) {

            }else {/* Will added  another functional*/}
        }
        return mainAst;
    }

    private AST analiseFunction(KeyWords returnType,Node parentNode, Token startToken, Iterator<Token> tokenIterator) {
        Function root =  new Function(startToken, parentNode, returnType, startToken.marking, analiseInput(tokenIterator));
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            switch (currentToken.type){
                case RETURN:
                    root.addChildNode(formReturn(tokenIterator, root));
                    break;
            }
        }
        return new AST(root);
    }

    private Map<KeyWords, String> analiseInput(Iterator<Token> tokenIterator){
        Map<KeyWords, String> inputs = new HashMap<>();
        tokenIterator.next();
        //TODO when added more than main func
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            if (currentToken.type == KeyWords.RPAR){return inputs;}
            Token nextToken = tokenIterator.next();
            switch (currentToken.type){
                case INT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);
                    }else{
                        throw new SyntaxError(String.format("name of param missed, raw %d", nextToken.line));
                    }
                    continue;
                case FLOAT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);
                    }else{
                        throw new SyntaxError(String.format("name of param missed, raw %d", nextToken.line));
                    }


            }

        }return null;

    }

    private Node formReturn(Iterator<Token> tokenIterator, Function parent){
        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();
            switch (token.type){
                case NUM:
                    checkForEnd(tokenIterator);
                    return new Node(new Num(KeyWords.RETURN, token.marking, token.line, parent.returnType), parent);
                default: throw new SyntaxError(String.format("Error return in row %d", token.line));
            }
        }
        return null;
    }

    private void analiseAssigment(Node parentNode){
        //there will be analise of assigment
    }

    private void checkForEnd(Iterator<Token> tokenIterator){
        Token symbol = tokenIterator.next();
        if (symbol.type != KeyWords.SEMICOLON && tokenIterator.next().type != KeyWords.RCBRAC){
            throw new SyntaxError(String.format("Semicolon missed and } in row %d", symbol.line));
        }
    }


    private Boolean isNextWord(Token token){

        if (token.type == KeyWords.LINE) {
            return token.marking.split(" ").length == 1;
        }
        return false;
    }
}
