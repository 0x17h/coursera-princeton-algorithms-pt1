import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node<Item> {
        public Node<Item> prev;
        public final Item item;
        public Node<Item> next;

        Node(Item item) {
            this(item, null, null);
        }

        Node(Item item, Node<Item> prev, Node<Item> next) {
            this.item = item;
            this.prev = prev;
            if (prev != null) {
                prev.next = this;
            }
            this.next = next;
            if (next != null) {
                next.prev = this;
            }
        }
    }

    private Node<Item> head = null;
    private Node<Item> tail = null;
    private int size = 0;


    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkInsert(item);
        head = new Node<Item>(item, null, head);
        if (++size == 1) {
            tail = head;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        checkInsert(item);
        tail = new Node<Item>(item, tail, null);
        if (++size == 1) {
            head = tail;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkRemove();
        Item item = head.item;
        if (head.next != null) {
            head.next.prev = null;
            head = head.next;
        }
        else {
            head = null;
            tail = null;
        }
        --size;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkRemove();
        Item item = tail.item;
        if (tail.prev != null) {
            tail.prev.next = null;
            tail = tail.prev;
        }
        else {
            head = null;
            tail = null;
        }
        --size;
        return item;
    }

    private void checkInsert(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkRemove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        DequeIterator(Node<Item> node) {
            current = node;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(head);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        System.out.println("Deque is empty: " + deque.isEmpty());

        deque.addFirst("c");
        deque.addFirst("b");
        deque.addLast("d");
        deque.addFirst("a");
        deque.addLast("e");

        StdOut.println("Deque size after 5 adds: " + deque.size());
        StdOut.println("Is deque empty after 5 adds: " + deque.isEmpty());

        Iterator<String> it = deque.iterator();
        while (it.hasNext()) {
            StdOut.print(it.next() + ", ");
        }

        StdOut.println();

        while (!deque.isEmpty()) {
            StdOut.print("Item removed: " + (deque.size() % 2 == 0
                                             ? deque.removeFirst() : deque.removeLast()) + ", ");
        }

        StdOut.println();
        StdOut.println("Deque size after 5 removes: " + deque.size());
        StdOut.println("Is deque empty after 5 removes: " + deque.isEmpty());
    }
}
