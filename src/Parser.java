
import java.util.*;

public class

Parser {
    ArrayList<Token> tokens;
    AST mainAst;
    HashMap<String, Map<String, Integer>> functionsAst;
    ListIterator<Token> tokenIterator;
    int conditionalCounter = 0;
    private int startStackIndex = -4;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public AST parse(){
        functionsAst = new HashMap<>();
        mainAst = new AST();
        for (tokenIterator = tokens.listIterator(); tokenIterator.hasNext();){
            Token current = tokenIterator.next();
            if (current.type == KeyWords.INT || current.type == KeyWords.FLOAT){
                Token next = tokenIterator.next();
                switch (next.type){
                    case MAIN:
                        mainAst.addChildNode(analiseFunction(current.type, mainAst));
                        continue;

                    case ID:
                        mainAst.addChildNode(analiseFunction(current.type, mainAst));
                }

            }else {
            throw new MySyntaxError(current.line, 0, "function or declaration expected");
            }
        }
        mainAst.setFunctions(functionsAst);
        return mainAst;
    }

    private FunctionDeclaration analiseFunction(KeyWords returnType,Node parentNode) {
        Token currentToken = tokenIterator.previous();
        tokenIterator.next();
        Token startToken = currentToken;
        String name = currentToken.marking;
        Map<String, Integer> arguments = analiseInput();
        compareWithExistDeclaration(currentToken.line, currentToken.column,name, arguments.values().size());
        if (currentToken.type == KeyWords.SEMICOLON){
            FunctionDeclaration declaration = new FunctionDeclaration(startToken,returnType,arguments,name, parentNode);
            functionsAst.put(name, arguments);
            return declaration;
        }
        isFunctionDefined(name,currentToken.line, currentToken.column);
        Function root =  new Function(startToken,returnType,arguments,name, parentNode);
        functionsAst.put(root.name, root.inputParam);
        currentToken = tokenIterator.next();
        if (currentToken.type != KeyWords.LCBRAC){
            throw new MySyntaxError(currentToken.line, currentToken.column,
                    "LCBRAC expected");
        }

        Compound newCompound = new Compound(currentToken, root,startStackIndex);
        parseCompound(newCompound);
        root.setChildCompound(newCompound);

        return root;
    }
    private void isFunctionDefined(String name, int raw, int column){
        if(mainAst.getChildNodes() == null)return;
        for (Node child:mainAst.getChildNodes()
             ) {
            if (child.getClass() == Function.class){
                Function function = (Function) child;
                if (function.name.equals(name)){
                    throw new MySyntaxError(raw,column,"Function with that name was defined earlier");
                }
            }
        }
    }

    private void compareWithExistDeclaration(int line, int column, String name, int size) {
        if (functionsAst.containsKey(name)){
            if (functionsAst.get(name).values().size() != size){
                throw new MySyntaxError(line,column,"That function was declared earlier with another num of parameters");
            }
        }
    }


    private void parseCompound(Compound root) {
        while (tokenIterator.hasNext()){
            Token currentToken = tokenIterator.next();
            switch (currentToken.type){
                case INT:
                case FLOAT:
                    root.addChildNode(parseDeclaration(root, currentToken.type));
                    break;
                case ID:
                    root.addChildNode(assignOrCall(root));
                    break;
                case RETURN:
                    ReturnNode returnNode = new ReturnNode(currentToken, root);
                    returnNode.addChildNode(formReturn(root));
                    root.addChildNode(returnNode);
                    break;
                case IF:
                    root.addChildNode(parseConditional(root));
                    break;
                case PREFIX_PLUS:
                    Assign assign;
                    try {
                        assign = root.getVariableAssign(tokenIterator.next().marking);
                    }catch (NullPointerException exception){
                        throw new MySyntaxError(currentToken.line, currentToken.column, "declared variable expected");
                    }
                    tokenIterator.next();
                    checkSemicolon();

                    root.addChildNode(new Assign(assign,
                            new BinaryExpression(new LinkOnVar(assign.stackPointer), KeyWords.PLUS,new Num(new Token(KeyWords.NUM,"1"))), root));
                    break;
                case LCBRAC:
                    Compound newCompound = new Compound(currentToken, root, root.stackIndex);
                    parseCompound(newCompound);
                    root.addChildNode(newCompound);
                    break;
                default:
                    if (currentToken.type != KeyWords.RCBRAC){
                        throw new MySyntaxError(currentToken.line, currentToken.column,
                                "error syntax");
                    }
                    return;
            }
        }
    }

    private Node assignOrCall(Compound root) {
        Token currentToken = tokenIterator.next();
        if (currentToken.type == KeyWords.EQUALS){
            tokenIterator.previous();
            return parseAssign(root);
        }
        if (currentToken.type == KeyWords.LPAR){
            tokenIterator.previous();
            return parseCallFunction(root);
        }
        throw new MySyntaxError(currentToken.line, currentToken.column,
                "error syntax's");
    }

    private FunctionCall parseCallFunction(Compound root) {
        Token currentToken = tokenIterator.previous();
        tokenIterator.next();
        String name = currentToken.marking;
        FunctionCall functionCall = new FunctionCall(name, parseParameters(root), root);
        return functionCall;
    }


    private ArrayList<Term> parseParameters(Compound root) {
        Token token = tokenIterator.next();
        if (token.type!= KeyWords.LPAR){
            throw new MySyntaxError(token.line, token.column, "Open scope expected");
        }
        ArrayList<Term> parameters = new ArrayList<>();
        token = tokenIterator.next();
        while (token.type == KeyWords.ID || token.type == KeyWords.NUM){
            tokenIterator.previous();
            parameters.add(parseTerm(root));

            token = tokenIterator.next();
            if (token.type != KeyWords.COMA) break;
            token = tokenIterator.next();
        }
        if (token.type != KeyWords.RPAR){
            throw new MySyntaxError(token.line, token.column, "Close scope expected");
        }

        return parameters;
    }

    private void checkComa() {
        Token token = tokenIterator.previous();
        if (token.type != KeyWords.COMA){
            throw new MySyntaxError(token.line, token.column, "Coma  expected");
        }
    }

    private void backIterator(){
        tokenIterator.previous();
        tokenIterator.next();
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
        Compound newCompound = new Compound(token, root, root.stackIndex);
        parseCompound(newCompound);
        result.setIfPart(newCompound);
        token = tokenIterator.next();
        if (token.type == KeyWords.ELSE){
            token = tokenIterator.next();
            if (token.type != KeyWords.LCBRAC){
                throw new MySyntaxError(token.line, token.column,
                        "LCBRAC expected");
            }
            newCompound = new Compound(token, root, root.stackIndex);
            parseCompound(newCompound);
            result.setElsePart(newCompound);
        }
        return result;
    }

    private Assign parseAssign(Compound rootCompound) {
        Token token = tokenIterator.previous();
        tokenIterator.next();//to deploy iterator
        Assign newAssign;
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
        newAssign = new Assign(assign, parseMathHierarchy(rootCompound), rootCompound);
        checkSemicolon();
        return newAssign;
    }

    private Assign parseDeclaration(Compound root, KeyWords type) {
        Token token = tokenIterator.next();

        if (token.type != KeyWords.ID){
            throw new MySyntaxError(token.line, 1, "name expected");
        }
        String name = token.marking;
        if (root.checkLocalVariables(name)){throw new MySyntaxError(token.line, token.column, "variable with that name was declared earlier");}
        Assign result = new EmptyAssign(name, type, root.stackIndex, root);
        root.stackIndex-=4;
        token = tokenIterator.next();

        if (token.type != KeyWords.EQUALS){
            root.addAssign(result,name);
            checkSemicolon();
            return result;
        }

        result = new Assign((EmptyAssign) result,parseMathHierarchy(root), root);
        root.addAssign(result,name);
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


    private Map<String,Integer> analiseInput(){
        HashMap<String,Integer> inputs = new HashMap<>();
        int local_param_offset = +8;

        Token currentToken = tokenIterator.next();
        if (currentToken.type != KeyWords.LPAR){
            throw new MySyntaxError( currentToken.line,currentToken.column,"open brace expected");
        }
        currentToken = tokenIterator.next();
        while (currentToken.type == KeyWords.INT || currentToken.type == KeyWords.FLOAT){
            KeyWords type = currentToken.type;
            currentToken = tokenIterator.next();
            if (currentToken.type != KeyWords.ID){
                throw new MySyntaxError( currentToken.line,currentToken.column,"some name expected");
            }
            inputs.put(currentToken.marking, local_param_offset);
            local_param_offset+=4;
            currentToken = tokenIterator.next();
            if (currentToken.type != KeyWords.COMA){
                break;
            }
            currentToken = tokenIterator.next();
        }
        if (currentToken.type!=KeyWords.RPAR){
            throw new MySyntaxError( currentToken.line,currentToken.column,"close brace expected");
        }
        return inputs;
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
                return variableOrCall(next, rootCompound);


            default:
                throw new MySyntaxError(next.line, next.column, "Some term expected");
        }
    }

    private Term variableOrCall(Token token, Compound root) {
        Token next = tokenIterator.next();
        if (next.type == KeyWords.LPAR){
            tokenIterator.previous();
            return parseCallFunction(root);
        }
        return parseVariableLink(token, root);
    }

    private Term parseVariableLink(Token next, Compound rootCompound) {
        if (rootCompound.checkForVariable(next.marking)){
            if (rootCompound.isVariableAssign(next.marking)){
                throw new MySyntaxError(next.line, next.column,
                        "variable \""+next.marking+ "\" wasn`t initialized and uses");
            }
            tokenIterator.previous();
            return new LinkOnVar(rootCompound.getDepthOfFirstUse(next.marking));
        }else {
            throw new MySyntaxError(next.line, next.column,
                    "variable with \""+next.marking+ "\" name wasn't declared earlier");
        }
    }


    private Boolean isNextWord(Token token){
        if (token.type == KeyWords.LINE) {
            return token.marking.split(" ").length == 1;
        }
        return false;
    }
}
