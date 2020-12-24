import java.util.HashMap;

public class Compound extends Node{
    Compound parentCompound;
    HashMap<String, Assign> elements;
    Function parentFunction;
    int stackIndex;

    public Compound(Token thisToken, Compound parentCompound, int stackIndex) {
        super(thisToken);
        this.parentCompound = parentCompound;
        this.stackIndex = stackIndex;
    }

    public Compound(Token thisToken, Function parentFunction, int stackIndex) {
        super(thisToken);
        this.parentFunction = parentFunction;
        this.stackIndex = stackIndex;
    }


    protected Function getParentFunction() {
        if (parentCompound == null){
            return parentFunction;
        }
        return parentCompound.getParentFunction();
    }

    public String addAssign(Assign assign, String name){
        elements = getOrCreate();
        elements.put(name,assign);
        return name;
    }

    private HashMap<String, Assign> getOrCreate(){
        if (elements == null) return new HashMap<>();
        return elements;
    }

    public boolean checkForVariable(String marking) {
        if(elements != null) if(elements.containsKey(marking)) return true;
        if (parentCompound == null) return parentFunction.checkInParam(marking);
        return parentCompound.checkForVariable(marking);
    }

    public Assign getVariableAssign(String marking) {
        if (checkLocalVariables(marking)){return elements.get(marking);}
        return parentCompound.getVariableAssign(marking);
    }

    public boolean changeToFullAssign(Assign assign, String marking) {
        if (checkLocalVariables(marking)){
            elements.put(marking, assign);
            return true;
        }
        return parentCompound.changeToFullAssign(assign, marking);
    }

    public boolean checkLocalVariables(String name) {
        if (elements == null) return false;
        return elements.containsKey(name);
    }

    @Override
    public String generateCode() {
        StringBuilder result = new StringBuilder();
        for (Node child:super.getChildNodes()
             ) {
            result.append(child.generateCode());
        }
        return result.append(resetLocalVariables()).toString();
    }

    private String resetLocalVariables() {
        if (elements == null) return "";
        return "";
        //return "\n ret "+ (elements.values().size()-1)*4+"\n";
    }

    public int getDepthOfFirstUse(String marking) {
        if(elements != null){
            if(elements.containsKey(marking)) return elements.get(marking).stackPointer;
        }
        if (parentFunction!=null){
            if(parentFunction.checkInParam(marking)){
                return parentFunction.inputParam.get(marking);
            }
        }
        return parentCompound.getDepthOfFirstUse(marking);
    }


    public boolean isVariableAssign(String marking) {
        if (checkLocalVariables(marking)){return elements.get(marking).equivalent==null;}
        if (parentFunction!=null){
            return !parentFunction.checkInParam(marking);
        }
        return parentCompound.isVariableAssign(marking);
    }

    @Override
    protected boolean isFunction(String functionName, int parameters) {
        if(parentCompound != null) return parentCompound.isFunction(functionName, parameters);
        return parentFunction.isFunction(functionName, parameters);
    }

    public int getId(){
        if (parentCompound == null){
            throw new NullPointerException();
        }
        return parentCompound.getId();
    }
}
