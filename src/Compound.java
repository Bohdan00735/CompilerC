import java.util.HashMap;

public class Compound extends Node{
    Compound parentCompound;
    HashMap<String, Assign> elements;

    int stackIndex;

    public Compound(Token thisToken, Compound parentCompound, int stackIndex) {
        super(thisToken);
        this.parentCompound = parentCompound;
        this.stackIndex = stackIndex;
    }

    public Compound(Token thisToken, Node parentNode, int stackIndex) {
        super(thisToken, parentNode);
        this.stackIndex = stackIndex;
    }


    protected Function getParentFunction() {
        if (parentCompound.getClass() == Function.class){
            return (Function)parentCompound;
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
        if (parentCompound == null) return false;
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
        return "\n ret "+ (elements.values().size()-1)*4+"\n";
    }

    public int getDepthOfFirstUse(String marking) {
        if(elements != null){
            if(elements.containsKey(marking)) return elements.get(marking).stackPointer;
        }
        return parentCompound.getDepthOfFirstUse(marking);
    }


}
