public class Queue {
    private Node head;
    private Node tail;

    public Queue() {
        this.head = null;
        this.tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void enqueue(int x, int y) {
        Node newNode = new Node(x, y, null);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    public Node dequeue() {
        if (isEmpty()) {
            return null;
        }
        Node temp = head;
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        return temp;
    }
}