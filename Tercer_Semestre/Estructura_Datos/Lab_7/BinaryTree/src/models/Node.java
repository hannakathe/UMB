package models;

public class Node {
    public Object data;
    public Node left, right;

    public Node(Object data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
