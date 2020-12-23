public class Assign extends Node {
    String name;
    Term equivalent;
    KeyWords type;


    public Assign(String name, KeyWords type) {
        this.name = name;
        this.type = type;
    }

    public Assign(String name, Term equivalent, KeyWords type) {
        this.name = name;
        this.equivalent = equivalent;
        this.type = type;
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
                "mov "+ name+",eax\n";
    }

    public String generateInitialisation(){
        return "\n"+name+" dd 0 dup(0) \n";
    }
}

