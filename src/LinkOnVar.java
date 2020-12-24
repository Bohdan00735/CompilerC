public class LinkOnVar extends Term{
    int link;

    public LinkOnVar(int link) {
        this.link = link;
    }

    @Override
    public String generateCode() {
        if (link>0) return "mov eax,[ebp+"+ link +
                "]\npush eax\n";
        return "mov eax,[ebp"+ link +
                "]\npush eax\n";
    }
}
