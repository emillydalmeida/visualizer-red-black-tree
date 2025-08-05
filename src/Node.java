public class Node <T> {
    T data;
    Node<T> left;
    Node<T> right;
    Node<T> parent;
    Color color;

    public Node() {
        this.left = null;
        this.right = null;
        this.parent = null;
        this.color = Color.RED;
    }

    public Node(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.color = Color.RED;
    }

}