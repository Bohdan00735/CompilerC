import java.util.ArrayList;

public class FunctionCall extends Term {
    String functionName;
    ArrayList<Term> parameters;

    public FunctionCall(String functionName, ArrayList<Term> arguments, Node parentNode, Token token) {
        super( token, parentNode);
        this.functionName = functionName;
        this.parameters = arguments;
    }

    public FunctionCall( String functionName, ArrayList<Term> parameters, Node parentNode) {
        super(parentNode);
        this.functionName = functionName;
        this.parameters = parameters;
    }

    @Override
    public String generateCode() {
        try {
            parentNode.isFunction(functionName, parameters.size());
        }catch (MySyntaxError error){
            error.line = parentNode.getToken().line;
            error.column = getToken().column;
            throw error;
        }

        StringBuilder result = new StringBuilder();
        for (int i = parameters.size()-1; i >=0 ; i--) {
            result.append(parameters.get(i).generateCode());
        }
        result.append("\ncall "+functionName+"\n push eax\n");
        return result.toString();
    }
}
