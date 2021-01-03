
import java.util.*;

public class

Parser {
    ArrayList<Token> tokens;
    AST mainAst;
    HashMap<String, Map<String, Integer>> functionsAst;
    ListIterator<Token> tokenIterator;
    int conditionalCounter = 0;
    int cyclesCounter = 0;
    int pointerCounter = 0;
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
                        break;

                    case ID:
                        mainAst.addChildNode(analiseFunction(current.type, mainAst));
                        break;
                    default:
                        throw new MySyntaxError(next.line, next.column, "name missed");
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
        currentToken = tokenIterator.next();
        compareWithExistDeclaration(currentToken.line, currentToken.column,name, arguments.values().size());
        if (currentToken.type == KeyWords.SEMICOLON){
            FunctionDeclaration declaration = new FunctionDeclaration(startToken,returnType,arguments,name, parentNode);
            functionsAst.put(name, arguments);
            return declaration;
        }
        isFunctionDefined(name,currentToken.line, currentToken.column);
        Function root =  new Function(startToken,returnType,arguments,name, parentNode);
        functionsAst.put(root.name, root.inputParam);

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
                case DO:
                    root.addChildNode(parseCycle(root));
                    break;
                case CONTINUE:
                    try{
                        root.getId();
                    }catch (NullPointerException e){
                        throw new MySyntaxError(currentToken.line, currentToken.column, "Use continue in cycles");
                    }
                    root.addChildNode(new ContinueInstruction(currentToken, root.getId()));
                    tokenIterator.next();
                    checkSemicolon();
                    break;
                case BREAK:
                    try{
                        root.getId();
                    }catch (NullPointerException e){
                        throw new MySyntaxError(currentToken.line, currentToken.column, "Use break in cycles");
                    }
                    root.addChildNode(new BreakInstruction(currentToken, root.getId()));
                    tokenIterator.next();
                    checkSemicolon();
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
                            new BinaryExpression(new LinkOnVar(assign.stackPointer), KeyWords.PLUS,new Num(new Token(KeyWords.NUM,"1")), pointerCounter), root));
                    pointerCounter++;
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

    private void parseWhile(DoCycle cycle) {
        Token token = tokenIterator.next();
        checkOpenScope(token);
        cycle.setExitCondition(parseMathHierarchy(cycle));
        checkCloseScope(tokenIterator.previous());
        tokenIterator.next();
        tokenIterator.next();//rebase iterator
        checkSemicolon();
    }


    private DoCycle parseCycle(Compound root) {
        Token next = tokenIterator.next();
         checkLCBRAC(next);
         DoCycle doCycle = new DoCycle(next, root, root.stackIndex, cyclesCounter);
         cyclesCounter++;
         parseCompound(doCycle);
         next = tokenIterator.next();
         if (next.type!= KeyWords.WHILE){
             throw  new MySyntaxError(next.line, next.column, "While expected after do part");
         }
         parseWhile(doCycle);
         return doCycle;
    }

    private Node assignOrCall(Compound root) {
        Token currentToken = tokenIterator.next();
        if (currentToken.type == KeyWords.EQUALS){
            tokenIterator.previous();
            return parseAssign(root);
        }
        if (currentToken.type == KeyWords.LPAR){
            tokenIterator.previous();
            FunctionCall call = parseCallFunction(root);
            tokenIterator.next();
            return call;
        }
        throw new MySyntaxError(currentToken.line, currentToken.column,
                "error syntax's");
    }

    private FunctionCall parseCallFunction(Compound root) {
        Token currentToken = tokenIterator.previous();
        tokenIterator.next();
        String name = currentToken.marking;
        FunctionCall functionCall = new FunctionCall(name, parseParameters(root), root, currentToken);
        return functionCall;
    }
    void checkOpenScope(Token token){
        if (token.type!= KeyWords.LPAR){
            throw new MySyntaxError(token.line, token.column, "Open scope expected");
        }
    }

    void checkCloseScope(Token token){
        if (token.type != KeyWords.RPAR){
            throw new MySyntaxError(token.line, token.column, "Close scope expected");
        }
    }

    private ArrayList<Term> parseParameters(Compound root) {
        Token token = tokenIterator.next();
        checkOpenScope(token);
        ArrayList<Term> parameters = new ArrayList<>();
        token = tokenIterator.next();
        while (token.type == KeyWords.ID || token.type == KeyWords.NUM){
            tokenIterator.previous();
            parameters.add(parseTerm(root));

            token = tokenIterator.next();
            if (token.type != KeyWords.COMA) break;
            token = tokenIterator.next();
        }
        checkCloseScope(token);

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
        checkLCBRAC(token);
        Compound newCompound = new Compound(token, root, root.stackIndex);
        parseCompound(newCompound);
        result.setIfPart(newCompound);
        token = tokenIterator.next();
        if (token.type == KeyWords.ELSE){
            token = tokenIterator.next();
            checkLCBRAC(token);
            newCompound = new Compound(token, root, root.stackIndex);
            parseCompound(newCompound);
            result.setElsePart(newCompound);
            tokenIterator.next();
        }
        tokenIterator.previous();
        return result;
    }

    public void checkLCBRAC(Token token){
        if (token.type != KeyWords.LCBRAC){
            throw new MySyntaxError(token.line, token.column,
                    "LCBRAC expected");
        }
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

        newAssign = new Assign(assign, checkTransformationOrParse(rootCompound), rootCompound);
        checkSemicolon();
        return newAssign;
    }

    Term checkTransformationOrParse(Compound rootCompound){
        if (tokenIterator.next().type==KeyWords.LPAR){
            if (tokenIterator.next().type == KeyWords.INT){
                checkCloseScope(tokenIterator.next());
                ArrayList<Term> arguments = new ArrayList<>();
                arguments.add(parseMathHierarchy(rootCompound));
                addConvertFunction();
                return new FunctionCall("convertToInt",arguments,rootCompound);
            }
            tokenIterator.previous();
        }
        tokenIterator.previous();
        return parseMathHierarchy(rootCompound);
    }

    private void addConvertFunction() {
        String name = "convertToInt";
        if (functionsAst.containsKey(name)) return;
        HashMap<String, Integer> param = new HashMap<>();
        param.put("number",8);
        Function function = new Function(mainAst,KeyWords.INT,param,name);
        functionsAst.put(name, param);
        mainAst.addChildNode(function);
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

        result = new Assign(result,checkTransformationOrParse(root), root);
        root.addAssign(result,name);
        checkSemicolon();

        return result;
    }

    private void checkSemicolon(){
        Token token = tokenIterator.previous();
        if (token.type != KeyWords.SEMICOLON){
            token = tokenIterator.previous();
            throw new MySyntaxError(token.line, token.column+1,"Semicolon expected at the end of declaration");
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
        while (next.type == KeyWords.OR || next.type == KeyWords.LESS_EQUALS){
            Term nextTerm = analiseMathExpresion(rootCompound);
            term = new BinaryExpression(term, next.type, nextTerm, pointerCounter);
            pointerCounter++;
            next = tokenIterator.previous();
            tokenIterator.next();
        }
        return term;
    }
    private Term analiseMathExpresion(Compound rootCompound){
        Term term = parseExpresion(rootCompound);
        tokenIterator.previous();
        Token next = tokenIterator.next();

        while (next.type == KeyWords.PLUS || next.type == KeyWords.MINUS){
            Term nextTerm = parseExpresion(rootCompound);
            term = new BinaryExpression(term, next.type, nextTerm, pointerCounter);
            pointerCounter++;
            next = tokenIterator.previous();
            tokenIterator.next();

        }
        return term;
    }

    private Term parseExpresion(Compound rootCompound){
        Term term = parseTerm(rootCompound);
        Token next = tokenIterator.next();
        while (next.type == KeyWords.DIVISION){
            Term nextTerm = parseTerm(rootCompound);
            term = new BinaryExpression(term, next.type, nextTerm, pointerCounter);
            pointerCounter++;
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
