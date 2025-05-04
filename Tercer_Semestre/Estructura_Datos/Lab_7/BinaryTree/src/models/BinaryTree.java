package models;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree {
    public Node root;

    public BinaryTree() {
        this.root = null;
    }

    public void insert(Object data) {
        root = insertRecursive(root, data);
    }

    private Node insertRecursive(Node node, Object data) {
        if (node == null) {
            return new Node(data);
        }

        if (data instanceof Integer) {
            if ((Integer) data < (Integer) node.data) {
                node.left = insertRecursive(node.left, data);
            } else {
                node.right = insertRecursive(node.right, data);
            }
        } else {
            if (data.toString().compareTo(node.data.toString()) < 0) {
                node.left = insertRecursive(node.left, data);
            } else {
                node.right = insertRecursive(node.right, data);
            }
        }
        return node;
    }

    public void inOrder() {
        inOrderRecursive(root);
    }

    private void inOrderRecursive(Node node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.print(node.data + " ");
            inOrderRecursive(node.right);
        }
    }

    public void preOrder() {
        preOrderRecursive(root);
    }

    private void preOrderRecursive(Node node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderRecursive(node.left);
            preOrderRecursive(node.right);
        }
    }

    public void postOrder() {
        postOrderRecursive(root);
    }

    private void postOrderRecursive(Node node) {
        if (node != null) {
            postOrderRecursive(node.left);
            postOrderRecursive(node.right);
            System.out.print(node.data + " ");
        }
    }

    public int calculateDegree() {
        return calculateDegreeRecursive(root);
    }

    private int calculateDegreeRecursive(Node node) {
        if (node == null) return 0;
        int left = calculateDegreeRecursive(node.left);
        int right = calculateDegreeRecursive(node.right);
        return Math.max(left, right) + 1;
    }

    public int calculateHeight() {
        return calculateHeightRecursive(root);
    }

    private int calculateHeightRecursive(Node node) {
        if (node == null) return 0;
        int left = calculateHeightRecursive(node.left);
        int right = calculateHeightRecursive(node.right);
        return Math.max(left, right) + 1;
    }

    public int calculateLevel(Object data) {
        return calculateLevelRecursive(root, data, 1);
    }

    private int calculateLevelRecursive(Node node, Object data, int level) {
        if (node == null) return 0;
        if (node.data.equals(data)) return level;
        int leftLevel = calculateLevelRecursive(node.left, data, level + 1);
        if (leftLevel != 0) return leftLevel;
        return calculateLevelRecursive(node.right, data, level + 1);
    }

    public void breadthFirstSearch() {
        if (root == null) return;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.data + " ");
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
    }
}
