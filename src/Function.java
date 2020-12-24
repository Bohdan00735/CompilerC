import java.util.HashMap;
import java.util.Map;

public class Function extends FunctionDeclaration{
    Compound childCompound;

    public Function(Token thisToken, KeyWords returnType, Map<String, Integer> inputParam, String name, Node parentNode) {
        super(thisToken, returnType, inputParam, name, parentNode);
    }

    public Function(Node root, KeyWords returnType, Map<String, Integer> input, String name) {
        super(root, returnType, input, name);
    }


    public void setChildCompound(Compound childCompound) {
        this.childCompound = childCompound;
    }



    @Override
    public String generateCode() {
        if (name.equals("main")){return "\n main proc\n" +
                childCompound.generateCode()+
                "main endp";}
        if (name.equals("convertToInt")) return getConvertToIntProc();
        return "\n" + name + " proc\n" +
                "push ebp\n" +
                "mov ebp, esp\n" + childCompound.generateCode()+
                "\n"+ name + " endp\n";
    }

    private String getConvertToIntProc() {
        return "convertToInt proc\n" +
                "push ebp\n" +
                "mov ebp, esp\n" +
                "mov eax,[ebp+8]\n" +
                "\n" +
                "mov ecx, eax\n" +
                "\n" +
                " shr ecx, 23\n" +
                "  and ecx, 0ffh\n" +
                " \n" +
                "  and eax, 007fffffh\n" +
                "  or eax, 00800000h\n" +
                "  sub ecx, 127\n" +
                " \n" +
                "  mov edx, ecx\n" +
                "  mov cl, 23\n" +
                "  sub cl, dl\n" +
                "  shr eax, cl \n" +
                "\n" +
                "mov ebp, esp \n" +
                "pop ebp\n" +
                "\n" +
                "ret 0\n" +
                "\n" +
                "convertToInt endp\n";
    }


}
