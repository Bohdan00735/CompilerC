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
        for (Iterator<Token> tokenIterator = tokens.iterator(); tokenIterator.hasNext();){
            Token current = tokenIterator.next();
            if (current.type == KeyWords.INT || current.type == KeyWords.FLOAT){
                Token next = tokenIterator.next();
                switch (next.type){
                    case MAIN: mainAst = analiseFunction(current.type, null, next, tokenIterator);
                    case LINE: //will create a param or func
                    default: throw new SyntaxError(String.format("Error syntax after type in raw %d",next.line));
                }

            }
            else if(current.type == KeyWords.RETURN) {

            }else {/* Will added  another functional*/}
        }
        return mainAst;
    }

    private AST analiseFunction(KeyWords returnType,Node parentNode, Token startToken, Iterator<Token> tokenIterator) {
        Node root =  new Function(startToken, parentNode, returnType, startToken.marking, analiseInput(tokenIterator));
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            switch (currentToken.type){
                case RETURN:
                    formReturn(tokenIterator, root);
            }
        }
        return null;
    }

    Map<KeyWords, String> analiseInput(Iterator<Token> tokenIterator){
        Map<KeyWords, String> inputs = new HashMap<>();
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
                case FLOAT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);
                    }else{
                        throw new SyntaxError(String.format("name of param missed, raw %d", nextToken.line));
                    }
                default: throw new SyntaxError(String.format("Error param in row %d", nextToken.line));
            }

        }return null;

    }

    private Node formReturn(Iterator<Token> tokenIterator, Node parent){
        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();
            switch (token.type){
                case NUM:
                    checkForEnd(tokenIterator);
                    return new Node(new Num(KeyWords.NUM, token.marking, token.line, parent.thisToken.type), parent);
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


    public Boolean isNextWord(Token token){

        if (token.type == KeyWords.LINE) {
            return token.marking.split(" ").length == 1;
        }
        return false;
    }
}
