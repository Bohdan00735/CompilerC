public class ContinueInstruction extends Instruction{
    public ContinueInstruction(Token thisToken, int cycleId) {
        super(thisToken, cycleId);
    }

    @Override
    public String generateCode() {
        return "jmp _endCycle"+cycleId+"\n";
    }
}
