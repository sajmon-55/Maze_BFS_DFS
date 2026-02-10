public class Stack {
    private Node top;

    public Stack() {
        top = null;
    }
    public boolean isEmpty() {
        return top == null;
    }
    public void push(int x, int y) {
        Node node = new Node(x, y, top);
        top = node;
    }
    public Node pop() {
        if (isEmpty())
            return null;
        Node temp = top;
        top = temp.getNext();
        return temp;
    }
    public Node peek() {
        return top;
    }
}
