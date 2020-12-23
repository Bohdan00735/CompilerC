import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node[] childNodes;
    private Token thisToken;
    Node parentNode;

    public Node(Token thisToken, Node parentNode) {
        this.thisToken = thisToken;
        this.parentNode = parentNode;
    }

    public Node(Token thisToken) {
        this.thisToken = thisToken;
    }

    public Node() {
    }

    public Node[] getChildNodes() {
        return childNodes;
    }

    public Token getToken() {
        return thisToken;
    }

    public void addChildNode(Token child){
        refactorList();
        childNodes[childNodes.length-1] = new Node(child, this);
    }

    public void addChildNode(Node child){
        refactorList();
        childNodes[childNodes.length-1] = child;
    }

    private void refactorList(){
        getOrCreate();
        Node[] newList = new Node[childNodes.length+1];
        System.arraycopy(childNodes, 0, newList, 0, childNodes.length);
        childNodes = newList;
    }
    private void getOrCreate() {
        if (childNodes == null) childNodes = new Node[0];
    }

    public String generateCode() {
        return null;
    }


}
