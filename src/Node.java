public class Node {
    private int x;
    private int y;
    private Node next;
    public Node(int x, int y, Node next) {
        this.x = x;
        this.y = y;
        this.next = next;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
