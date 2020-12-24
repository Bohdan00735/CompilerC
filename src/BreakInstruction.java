public class BreakInstruction extends Instruction{
    public BreakInstruction(Token thisToken, int cycleId) {
        super(thisToken, cycleId);
    }

    @Override
    public String generateCode() {
        return "jmp _exitCycle"+cycleId+"\n";
    }
}
