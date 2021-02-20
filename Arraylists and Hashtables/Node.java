public class Node<T extends Comparable<T>> {

    // instance variables
    private T data;
    private Node<T> next;
    private int index;

    // constructors
    public Node() {}

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.index = 0;
    }

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.index = 0;
    }

    // selectors
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public String toString() {//debugging purposes only
        Node<T> current = this;
        String output = "";

        while (current != null) {
            output += current.getData() + ", ";
            current = current.getNext();
        }

        return output;
    }
}

//written by halvo561 and doepn008