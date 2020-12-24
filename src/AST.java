import java.util.HashMap;
import java.util.Map;

public class AST extends Node {
    HashMap<String, Map<String, Integer>> functions;

    public void setFunctions(HashMap<String, Map<String, Integer>> functions) {
        this.functions = functions;
    }

    @Override
    protected boolean isFunction(String functionName, int parameters) {
        if (!functions.containsKey(functionName)){
            throw new MySyntaxError(0,0,"No functions with that name");
        }
        if (functions.get(functionName).values().size() != parameters){
            throw new MySyntaxError(0,0,String.format("Function %s has another num of parameters",functionName));
        }
        return true;
    }

    @Override
    public String generateCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node :getChildNodes()
             ) {
            stringBuilder.append(node.generateCode());
        }
        return stringBuilder.toString();
    }
}
