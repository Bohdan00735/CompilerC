import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childNodes;
    private Token thisToken;
    Node parentNode;

    public Node(Token thisToken, Node parentNode) {
        this.thisToken = thisToken;
        this.parentNode = parentNode;
        childNodes = new ArrayList<>();
    }

    public ArrayList<Node> getChildNodes() {
        return childNodes;
    }

    public Token getToken() {
        return thisToken;
    }

    public void addChildNode(Token child){
        childNodes.add(new Node(child, this));
    }
}
