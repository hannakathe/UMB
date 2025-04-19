package models;

public class CircularList {
    private Node head = null;

    public void insert(int value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != head) temp = temp.next;
            temp.next = newNode;
            newNode.next = head;
        }
    }

    public boolean delete(int value) {
        if (head == null) return false;
        if (head.value == value && head.next == head) {
            head = null;
            return true;
        }
        Node current = head, prev = null;
        do {
            if (current.value == value) {
                if (prev != null) prev.next = current.next;
                else {
                    Node tail = head;
                    while (tail.next != head) tail = tail.next;
                    head = head.next;
                    tail.next = head;
                }
                return true;
            }
            prev = current;
            current = current.next;
        } while (current != head);
        return false;
    }

    public boolean update(int oldValue, int newValue) {
        Node current = head;
        if (head == null) return false;
        do {
            if (current.value == oldValue) {
                current.value = newValue;
                return true;
            }
            current = current.next;
        } while (current != head);
        return false;
    }

    public int search(int value) {
        Node current = head;
        int pos = 0;
        if (head == null) return -1;
        do {
            if (current.value == value) return pos;
            pos++;
            current = current.next;
        } while (current != head);
        return -1;
    }

    public void sort() {
        if (head == null || head.next == head) return;
        boolean swapped;
        do {
            swapped = false;
            Node current = head;
            do {
                Node nextNode = current.next;
                if (current != nextNode && current.value > nextNode.value) {
                    int temp = current.value;
                    current.value = nextNode.value;
                    nextNode.value = temp;
                    swapped = true;
                }
                current = current.next;
            } while (current.next != head);
        } while (swapped);
    }

    public void clear() {
        head = null;
    }

    public String toString() {
        if (head == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        do {
            sb.append(current.value).append(", ");
            current = current.next;
        } while (current != head);
        sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }
}
