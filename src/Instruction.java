public class Instruction extends Node{
    int cycleId;

    public Instruction(Token thisToken, int cycleId) {
        super(thisToken);
        this.cycleId = cycleId;
    }
}
