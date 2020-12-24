import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionDeclaration extends Node{
    KeyWords returnType;
    Map<String, Integer> inputParam;
    String name;

    public FunctionDeclaration(Token thisToken, KeyWords returnType, Map< String, Integer> inputParam, String name, Node parentNode) {
        super(thisToken, parentNode);
        this.returnType = returnType;
        this.inputParam = inputParam;
        this.name = name;
    }

    @Override
    public String generateCode() {
        return "";
    }

    public boolean checkInParam(String name){
        if (inputParam == null)return false;
        try {
            if(inputParam.get(name) == null) return false;
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

}
