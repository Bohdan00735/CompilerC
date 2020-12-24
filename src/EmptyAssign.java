public class EmptyAssign extends Assign{
    public EmptyAssign(String name, KeyWords type, int stackPointer, Node parent) {
        super(name, type, stackPointer, parent);
    }

    @Override
    public String generateCode() {
        return "";
    }
}
