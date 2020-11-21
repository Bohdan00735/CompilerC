import jdk.internal.org.jline.reader.SyntaxError;

import javax.print.DocFlavor;
import java.lang.reflect.Parameter;
import java.security.Key;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

public class

Parser {
    ArrayList<Token> tokens;
    AST mainAst;
    HashMap<String, AST> functionsAst;
    Iterator<Token> tokenIterator;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public AST parse(){
        functionsAst = new HashMap<>();
        for (tokenIterator = tokens.iterator(); tokenIterator.hasNext();){
            Token current = tokenIterator.next();
            if (current.type == KeyWords.INT || current.type == KeyWords.FLOAT){
                Token next = tokenIterator.next();
                switch (next.type){
                    case MAIN:
                        mainAst = analiseFunction(current.type, null, next, tokenIterator);
                        continue;

                    case LINE:
                        functionsAst.put(current.marking, analiseFunction(current.type, mainAst.getRoot(), next, tokenIterator));

                        //TODO for param
                }

            }else {
            throw new MySyntaxError(current.line, 0, "function or declaration expected");
            }
        }
        return mainAst;
    }

    private AST analiseFunction(KeyWords returnType,Node parentNode, Token startToken, Iterator<Token> tokenIterator) {
        Function root =  new Function(startToken, parentNode, returnType, startToken.marking, analiseInput(tokenIterator));
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            switch (currentToken.type){
                case INT:
                    root.addChildNode(parseDeclaration(tokenIterator, root, currentToken.type));
                case RETURN:
                    root.addChildNode(formReturn(tokenIterator, root));
                    break;
            }
        }
        return new AST(root);
    }

    private Node parseDeclaration(Iterator<Token> tokenIterator, Function root, KeyWords type) {
        Token token = tokenIterator.next();
        if (token.type != KeyWords.LINE){
            throw new MySyntaxError(token.line, 1, "name expected");
        }
        String name = token.marking;
        token = tokenIterator.next();
        if (token.type != KeyWords.EQUALS){
            throw new MySyntaxError(token.line, 2, "equals symbol expected");
        }
        Node result;
        if(isNextNum(tokenIterator)){
            result = new Node(tokenIterator.next(),root);
        }else {
            result = new Node(new Num(type, name, token.line, token.column),root);
            result.addChildNode(analiseMathExpresion(tokenIterator));
        }

        if (tokenIterator.next().type != KeyWords.SEMICOLON){
            throw new MySyntaxError(token.line, -1,"Semicolon expected at the end of declaration");
        }
        return result;
    }


    private Map<KeyWords, String> analiseInput(Iterator<Token> tokenIterator){
        Map<KeyWords, String> inputs = new HashMap<>();
        tokenIterator.next();
        int column = 0;
        //TODO when added more than main func
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            if (currentToken.type == KeyWords.RPAR){return inputs;}
            Token nextToken = tokenIterator.next();
            switch (currentToken.type){
                case INT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);

                    }else{
                        throw new MySyntaxError( nextToken.line,column,"name of param missed");
                    }
                    continue;
                case FLOAT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);
                    }else{
                        throw new MySyntaxError(nextToken.line,column,"name of param missed");
                    }


            }
            column++;

        }return null;

    }

    private Node formReturn(Iterator<Token> tokenIterator, Function parent){
        if(isExpresion(tokenIterator)){
            return analiseExpression(tokenIterator, new Expression(
                    new Token(KeyWords.RETURN,"return", 0,0),parent, KeyWords.RETURN));
        }
        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();
            int column = 0;
            switch (token.type){
                case NUM:
                    if (checkForEnd(tokenIterator)){
                        return new Node(new Num(KeyWords.RETURN, token.marking, token.line, token.column,parent.returnType), parent);}
                    else {
                        throw new MySyntaxError(token.line,column,("End symbol expected row %d"));
                    }

                default: throw new MySyntaxError(token.line,column,"Error return in row %d");
            }
        }
        return null;
    }

    private boolean isExpresion(Iterator<Token> tokenIterator) {
        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();
            switch (token.type){
                case NUM:
                    isNextBin(tokenIterator);
                    return true;
                case EXCLAMATION_POINT:
                    isNextNum(tokenIterator);
            }
        }
        return false;
    }

    private boolean isNextNum(Iterator<Token> tokenIterator) {
        return tokenIterator.next().type == KeyWords.NUM;
    }

    private Expression analiseExpression(Iterator<Token> tokenIterator, Expression parent) {
        int column = 0;
        Token token = tokenIterator.next();
        if (token.type == KeyWords.FLOAT || token.type == KeyWords.INT){
            KeyWords type = token.type;
            token = tokenIterator.next();
            column++;
            if (token.type != KeyWords.LINE){
                throw new MySyntaxError(token.line, column,"Name in declare expected");
            }
        }
        return null;
    }


    private Term analiseMathExpresion(Iterator<Token> tokenIterator){
        Term term = parseTerm(tokenIterator);
        Token next = tokenIterator.next();
        while (next.type == KeyWords.PLUS){
            Term nextTerm = parseTerm(tokenIterator);
            term = new BinaryExpression(term, next.type, nextTerm);
            next = tokenIterator.next();
        }
        return term;
    }

    private Term parseTerm(Iterator<Token> tokenIterator) {
        Term result;
        Token next = tokenIterator.next();
        switch (next.type){
            case LCBRAC:
                result = parseTerm(tokenIterator);
                next = tokenIterator.next();
                if (next.type != KeyWords.RCBRAC){
                    throw new MySyntaxError(next.line, next.column, "Close brace expected");
                }
                return result;
            case EXCLAMATION_POINT:
                result = new Term(next);
                result.setExpression(new UnaryExpression(next.type,parseTerm(tokenIterator)));
                return result;
            case NUM:
                return new Term(next);
            default:
                throw new MySyntaxError(next.line, next.column, "Some term expected");
        }
    }

    private boolean isEndLine(Iterator<Token> tokenIterator) {
        return tokenIterator.next().type == KeyWords.EXCLAMATION_POINT;
    }

    private boolean isNextBin(Iterator<Token> tokenIterator) {
        return tokenIterator.next().type != KeyWords.PLUS;
    }

    private void analiseAssigment(Node parentNode){
        //there will be analise of assigment
    }

    private Boolean checkForEnd(Iterator<Token> tokenIterator){
        Token symbol = tokenIterator.next();
        if (symbol.type != KeyWords.SEMICOLON && tokenIterator.next().type != KeyWords.RCBRAC){
            return true;
        }return false;
    }


    private Boolean isNextWord(Token token){

        if (token.type == KeyWords.LINE) {
            return token.marking.split(" ").length == 1;
        }
        return false;
    }
}
