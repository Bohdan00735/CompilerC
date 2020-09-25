import java.util.ArrayList;

public class Node {
    ArrayList<Node> childNodes;
    Token thisToken;
    Node parentNode;

    public Node(Token thisToken, Node parentNode) {
        this.thisToken = thisToken;
        this.parentNode = parentNode;
        childNodes = new ArrayList<>();
    }



    public void addChildNode(Token child){
        childNodes.add(new Node(child, this));
    }
}
