public class Term extends  Node {

    public Term(Token thisToken, Node parentNode) {
        super(thisToken, parentNode);
    }




    public Term() {
    }

    public Term(Node parentNode) {
        super(parentNode);
    }

    public Term(Token thisToken) {
        super(thisToken);
    }
}
