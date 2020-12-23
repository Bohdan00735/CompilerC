public class EmptyAssign extends Assign{
    public EmptyAssign(String name, KeyWords type, int stackPointer) {
        super(name, type, stackPointer);
    }

    @Override
    public String generateCode() {
        return "";
    }
}
