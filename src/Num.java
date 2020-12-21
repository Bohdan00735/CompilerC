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
        String num = super.getToken().marking;
        if (isNumerical(num)){return "\n\r push  " + num +";";}
        if (isFloat(num)){
            int intBits = Float.floatToIntBits(Float.parseFloat(super.getToken().marking));
            String binary = Integer.toBinaryString(intBits)+"b";
            return  "\n\r push  " + binary +";";
        }
        throw new MySyntaxError(super.getToken().line, super.getToken().column, "Not allowed type");
    }

    boolean isNumerical(String num){
        try
        {
            Integer.parseInt(num);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    boolean isFloat(String num){
        try
        {
            Float.parseFloat(num);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
