public class Assign extends Node {
    String name;
    Term equivalent;
    KeyWords type;
    int stackPointer;


    public Assign(String name, KeyWords type, int stackPointer) {
        this.name = name;
        this.type = type;
        this.stackPointer= stackPointer;
    }

    public Assign(EmptyAssign emptyAssign,Term equivalent) {
        name = emptyAssign.name;
        type = emptyAssign.type;
        stackPointer = emptyAssign.stackPointer;
        this.equivalent = equivalent;
    }

    public String getName() {
        return name;
    }

    public void setEquivalent(Term equivalent) {
        this.equivalent = equivalent;
    }

    @Override
    public String generateCode() {
        if (equivalent == null){
            return "";
        }
        return equivalent.generateCode()+
                "\n\rpop eax;\n" +
                "mov [ebp"+ stackPointer+"],eax\n";
    }
}

