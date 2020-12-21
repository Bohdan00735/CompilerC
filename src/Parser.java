
import javax.print.DocFlavor;
import java.lang.reflect.Parameter;
import java.security.Key;
import java.security.KeyException;
import java.util.*;
import java.util.function.ToDoubleBiFunction;

public class

Parser {
    ArrayList<Token> tokens;
    AST mainAst;
    HashMap<String, AST> functionsAst;
    HashMap<String, Assign> elements = new HashMap<>();
    ListIterator<Token> tokenIterator;
    int openedScopes = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public AST parse(){
        functionsAst = new HashMap<>();
        for (tokenIterator = tokens.listIterator(); tokenIterator.hasNext();){
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
                case FLOAT:
                    root.addChildNode(parseDeclaration(root, currentToken.type));
                    break;
                case ID:
                    root.addChildNode(parseAssign());
                    break;
                case RETURN:
                    Node returnNode = new Node(currentToken, parentNode);
                    returnNode.addChildNode(formReturn(root));
                    root.addChildNode(returnNode);
                    break;
            }
        }
        return new AST(root);
    }

    private Assign parseAssign() {
        Token token = tokenIterator.previous();
        tokenIterator.next();//to deploy iterator
        Assign assign;
        try {
            assign = elements.get(token.marking);
        }catch (NullPointerException exception){
            throw new MySyntaxError(token.line, token.column, "no variables with that name");
        }
        token = tokenIterator.next();
        if (token.type!=KeyWords.EQUALS){
            throw new MySyntaxError(token.line, token.column, "Equals symbol expected");
        }
        assign.setEquivalent(analiseMathExpresion());
        checkSemicolon();
        return assign;
    }

    private Assign parseDeclaration(Function root, KeyWords type) {
        Token token = tokenIterator.next();

        if (token.type != KeyWords.ID){
            throw new MySyntaxError(token.line, 1, "name expected");
        }
        String name = token.marking;
        if (elements.containsKey(name)){throw new MySyntaxError(token.line, token.column, "variable with that name was declared earlier");}
        Assign result = new Assign(name, type);
        elements.put(name, result);
        token = tokenIterator.next();

        if (token.type != KeyWords.EQUALS){
            checkSemicolon();
            return result;
        }

        result.setEquivalent(parseMathHierarchy());
        checkSemicolon();

        return result;
    }

    private void checkSemicolon(){
        Token token = tokenIterator.previous();
        if (token.type != KeyWords.SEMICOLON){
            throw new MySyntaxError(token.line, token.column,"Semicolon expected at the end of declaration");
        }
    }


    private Map<KeyWords, String> analiseInput(Iterator<Token> tokenIterator){
        Map<KeyWords, String> inputs = new HashMap<>();
        tokenIterator.next();

        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            if (currentToken.type == KeyWords.RPAR){return inputs;}
            Token nextToken = tokenIterator.next();
            switch (currentToken.type){
                case INT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);

                    }else{
                        throw new MySyntaxError( nextToken.line,nextToken.column,"name of param missed");
                    }
                    continue;
                case FLOAT:

                    if (isNextWord(nextToken) != null){inputs.put(KeyWords.INT,nextToken.marking);
                    }else{
                        throw new MySyntaxError(nextToken.line,nextToken.column,"name of param missed");
                    }
            }

        }return null;

    }

    private Node formReturn(Function parent){
        return parseMathHierarchy();

    }
    private Term parseMathHierarchy(){
        Term term = analiseMathExpresion();
        Token next = tokenIterator.previous();
        tokenIterator.next();
        while (next.type == KeyWords.OR){
            Term nextTerm = analiseMathExpresion();
            term = new BinaryExpression(term, next.type, nextTerm);
            next = tokenIterator.previous();
        }
        tokenIterator.next();
        return term;
    }
    private Term analiseMathExpresion(){
        Term term = parseTerm();
        Token next = tokenIterator.next();
        while (next.type == KeyWords.PLUS || next.type == KeyWords.MINUS){
            Term nextTerm = parseTerm();
            term = new BinaryExpression(term, next.type, nextTerm);
            next = tokenIterator.next();
        }
        return term;
    }

    private Term parseTerm() {
        Token next = tokenIterator.next();
        switch (next.type){
            case LPAR:
                Term result = parseMathHierarchy();
                next = tokenIterator.previous();
                if (next.type != KeyWords.RPAR){
                    throw new MySyntaxError(next.line, next.column, "Close brace expected");
                }
                return result;
            case EXCLAMATION_POINT:
                return new UnaryExpression(next.type,parseTerm());
            case NUM:
                return new Num(next);
            case ID:
                if (elements.containsKey(next.marking)){
                    if (elements.get(next.marking).equivalent == null){
                        throw new MySyntaxError(next.line, next.column,
                                "variable \""+next.marking+ "\" wasn`t initialized and uses");
                    }
                    return new LinkOnVar(next.marking);
                }else {
                    throw new MySyntaxError(next.line, next.column,
                            "variable with \""+next.marking+ "\" name wasn't declared earlier");
                }

            default:
                throw new MySyntaxError(next.line, next.column, "Some term expected");
        }
    }



    private Boolean isNextWord(Token token){

        if (token.type == KeyWords.LINE) {
            return token.marking.split(" ").length == 1;
        }
        return false;
    }
}
