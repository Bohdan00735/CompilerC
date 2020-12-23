
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
    ArrayList<HashMap<String, Assign>> encloseVariables = new ArrayList<>();
    ListIterator<Token> tokenIterator;
    int conditionalCounter = 0;


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
                        mainAst = analiseFunction(current.type, null, next,0);
                        continue;

                    case LINE:
                        functionsAst.put(current.marking, analiseFunction(current.type, mainAst.getRoot(), next,0));

                        //TODO for param
                }

            }else {
            throw new MySyntaxError(current.line, 0, "function or declaration expected");
            }
        }
        return mainAst;
    }

    private AST analiseFunction(KeyWords returnType,Node parentNode, Token startToken, int depth) {
        Function root =  new Function(startToken, parentNode,depth, returnType, startToken.marking, analiseInput(tokenIterator));
        Token currentToken = tokenIterator.next();
        if (currentToken.type != KeyWords.LCBRAC){
            throw new MySyntaxError(currentToken.line, currentToken.column,
                    "LCBRAC expected");
        }
        while (currentToken.type == KeyWords.LCBRAC){
            Compound newCompound = new Compound(currentToken, root, root.getDepth());
            parseCompound(newCompound);
            encloseVariables.add(newCompound.elements);
            root.addChildNode(newCompound);
            if (!tokenIterator.hasNext()){return new AST(root);}
            currentToken = tokenIterator.next();
        }

        return new AST(root);
    }


    private Compound parseCompound(Compound root) {
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            switch (currentToken.type){
                case INT:
                case FLOAT:
                    root.addChildNode(parseDeclaration(root, currentToken.type));
                    break;
                case ID:
                    root.addChildNode(parseAssign(root));
                    break;
                case RETURN:
                    ReturnNode returnNode = new ReturnNode(currentToken, root);
                    returnNode.addChildNode(formReturn(root));
                    root.addChildNode(returnNode);
                    break;
                case IF:
                    root.addChildNode(parseConditional(root));
                    break;
                case LCBRAC:
                    Compound newCompound = new Compound(currentToken, root, root.getDepth()+1);
                    parseCompound(newCompound);
                    root.addChildNode(newCompound);
                    break;
                default:
                    if (currentToken.type != KeyWords.RCBRAC){
                        throw new MySyntaxError(currentToken.line, currentToken.column,
                                "error syntax");
                    }
                    return null;
            }
        }
        return null;
    }

    private Conditional parseConditional(Compound root) {
        Token token = tokenIterator.next();
        if (token.type != KeyWords.LPAR){
            throw new MySyntaxError(token.line, token.column, "Open scope expected");
        }
        Conditional result = new Conditional(token, root, parseMathHierarchy(root), conditionalCounter);
        conditionalCounter++;
        if (tokenIterator.previous().type != KeyWords.RPAR){
            throw new MySyntaxError(token.line, token.column, "Close scope expected");
        }
        tokenIterator.next();
        token = tokenIterator.next();
        if (token.type != KeyWords.LCBRAC){
            throw new MySyntaxError(token.line, token.column,
                    "LCBRAC expected");
        }
        Compound newCompound = new Compound(token, root, root.getDepth()+1);
        parseCompound(newCompound);
        result.setIfPart(newCompound);
        token = tokenIterator.next();
        if (token.type == KeyWords.ELSE){
            token = tokenIterator.next();
            if (token.type != KeyWords.LCBRAC){
                throw new MySyntaxError(token.line, token.column,
                        "LCBRAC expected");
            }
            newCompound = new Compound(token, root, root.getDepth()+1);
            parseCompound(newCompound);
            result.setElsePart(newCompound);
        }
        return result;
    }

    private Assign parseAssign(Compound rootCompound) {
        Token token = tokenIterator.previous();
        tokenIterator.next();//to deploy iterator
        Assign assign;
        try {
            assign = rootCompound.getVariableAssign(token.marking);
        }catch (NullPointerException exception){
            throw new MySyntaxError(token.line, token.column, "no variables with that name");
        }
        token = tokenIterator.next();
        if (token.type!=KeyWords.EQUALS){
            throw new MySyntaxError(token.line, token.column, "Equals symbol expected");
        }
        assign.setEquivalent(parseMathHierarchy(rootCompound));
        checkSemicolon();
        return assign;
    }

    private Assign parseDeclaration(Compound root, KeyWords type) {
        Token token = tokenIterator.next();

        if (token.type != KeyWords.ID){
            throw new MySyntaxError(token.line, 1, "name expected");
        }
        String name = token.marking;
        if (root.checkLocalVariables(name)){throw new MySyntaxError(token.line, token.column, "variable with that name was declared earlier");}
        Assign result = new Assign(name+root.getDepth(), type);
        root.addAssign(result,name);
        token = tokenIterator.next();

        if (token.type != KeyWords.EQUALS){
            checkSemicolon();
            return result;
        }

        result.setEquivalent(parseMathHierarchy(root));
        checkSemicolon();

        return result;
    }

    private void checkSemicolon(){
        Token token = tokenIterator.previous();
        if (token.type != KeyWords.SEMICOLON){
            throw new MySyntaxError(token.line, token.column,"Semicolon expected at the end of declaration");
        }
        tokenIterator.next();
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

    private Node formReturn(Compound rootCompound){
        Node res = parseMathHierarchy(rootCompound);
        checkSemicolon();
        return res;

    }

    private Term parseMathHierarchy(Compound rootCompound){
        Term term = analiseMathExpresion(rootCompound);
        tokenIterator.previous();
        Token next = tokenIterator.next();
        boolean toDeploy = false;
        while (next.type == KeyWords.OR){
            Term nextTerm = analiseMathExpresion(rootCompound);
            term = new BinaryExpression(term, next.type, nextTerm);
            next = tokenIterator.previous();
            toDeploy = true;
        }
        if (toDeploy) tokenIterator.next();
        return term;
    }
    private Term analiseMathExpresion(Compound rootCompound){
        Term term = parseTerm(rootCompound);
        Token next = tokenIterator.next();
        while (next.type == KeyWords.PLUS || next.type == KeyWords.MINUS){
            Term nextTerm = parseTerm(rootCompound);
            term = new BinaryExpression(term, next.type, nextTerm);
            next = tokenIterator.next();
        }
        return term;
    }

    private Term parseTerm(Compound rootCompound) {
        Token next = tokenIterator.next();
        switch (next.type){
            case LPAR:
                Term result = parseMathHierarchy(rootCompound);
                next = tokenIterator.previous();
                tokenIterator.next();
                if (next.type != KeyWords.RPAR){
                    throw new MySyntaxError(next.line, next.column, "Close brace expected");
                }
                return result;
            case EXCLAMATION_POINT:
                return new UnaryExpression(next.type,parseTerm(rootCompound));
            case NUM:
                return new Num(next);
            case ID:
                if (rootCompound.checkForVariable(next.marking)){
                    if (rootCompound.getVariableAssign(next.marking).equivalent == null){
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
