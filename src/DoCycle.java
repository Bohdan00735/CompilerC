public class DoCycle extends Compound{
    Term exitCondition;
    int id;

    public void setExitCondition(Term exitCondition) {
        this.exitCondition = exitCondition;
    }


    public DoCycle(Token thisToken, Compound parentCompound, int stackIndex, int id) {
        super(thisToken, parentCompound, stackIndex);
        this.id = id;
    }

    public DoCycle(Token thisToken, Function parentFunction, int stackIndex, int id) {
        super(thisToken, parentFunction, stackIndex);
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String generateCode() {
        StringBuilder result = new StringBuilder("\n_startCycle" + id + ":\n");
        for (Node child:super.getChildNodes()
             ) {
            result.append(child.generateCode());
        }
        result.append("\n _endCycle"+id+":\n"+ exitCondition.generateCode()+
                "\n pop eax\n"+
                "cmp eax, 0\n" +
                "je _startCycle"+id+"\n"+
                "_exitCycle"+id+":\n");

        return result.toString();
    }
}
