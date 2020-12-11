public class Num extends Term {
    KeyWords format;
    String name;

    public Num(Token thisToken, Node parentNode) {
        super(thisToken, parentNode);
    }

    public Num(Token thisToken) {
        super(thisToken);
    }

    public Num(Token thisToken, KeyWords format) {
        super(thisToken);
        this.format = format;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Num() {
    }

    @Override
    public String generateCode() {
        String result = "\n\r mov eax, ";
        int intBits = Float.floatToIntBits(Float.parseFloat(super.getToken().marking));
        String binary = Integer.toBinaryString(intBits)+"b";

        return  result + binary +";";
    }
}
