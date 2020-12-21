public class LinkOnVar extends Term{
    String link;

    public LinkOnVar(String link) {
        this.link = link;
    }

    @Override
    public String generateCode() {
        return "push "+ link;
    }
}
