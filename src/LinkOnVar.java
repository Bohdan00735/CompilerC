public class LinkOnVar extends Term{
    int link;

    public LinkOnVar(int link) {
        this.link = link;
    }

    @Override
    public String generateCode() {
        return "mov eax,[ebp"+ link +
                "]\npush eax\n";
    }
}
