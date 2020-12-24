import java.util.Map;

public class Function extends FunctionDeclaration{
    Compound childCompound;

    public Function(Token thisToken, KeyWords returnType, Map<String, Integer> inputParam, String name, Node parentNode) {
        super(thisToken, returnType, inputParam, name, parentNode);
    }

    public void setChildCompound(Compound childCompound) {
        this.childCompound = childCompound;
    }



    @Override
    public String generateCode() {
        return "\n" + name + " proc\n" +
                "push ebp\n" +
                "mov ebp, esp\n" + childCompound.generateCode()+
                "\n"+ name + " endp\n";
    }


}
