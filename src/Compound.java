import java.util.HashMap;

public class Compound extends Node{
    Compound parentCompound;
    HashMap<String, Assign> elements;
    private final int depth;

    public Compound(Token thisToken, Compound parentCompound, int depth) {
        super(thisToken);
        this.parentCompound = parentCompound;
        this.depth = depth;
    }

    public Compound(Token thisToken, Node parentNode, int depth) {
        super(thisToken, parentNode);
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
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
        return name+depth;
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
        StringBuilder stringBuilder = new StringBuilder();
        for (Assign as:elements.values()
             ) {
            stringBuilder.append("\n mov "+ as.name+", 0");
        }
        return stringBuilder.toString();
    }
}
