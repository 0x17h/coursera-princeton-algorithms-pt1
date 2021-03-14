import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] objects;
    private int size = 0;
    private int capacity = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (size == capacity) {
            resize(capacity == 0 ? 1 : capacity * 2);
        }
        objects[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        checkRemove();

        final int index = randomIndex();
        Item item = objects[index];
        objects[index] = objects[--size];
        objects[size] = null;

        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkRemove();
        return objects[randomIndex()];
    }

    private void checkRemove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private int randomIndex() {
        return StdRandom.uniform(0, size);
    }

    private void resize(int newCapacity) {
        Item[] newObjects = newCapacity != 0
                            ? (Item[]) new Object[newCapacity]
                            : null;
        if (newCapacity > 0) {
            for (int i = 0; i < size; ++i) {
                newObjects[i] = objects[i];
            }
        }

        objects = newObjects;
        capacity = newCapacity;
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private Item[] objects;
        private int index = 0;

        RandomizedQueueIterator(RandomizedQueue<Item> queue) {
            if (queue.size > 0) {
                this.objects = (Item[]) new Object[queue.size];
                for (int i = 0; i < this.objects.length; ++i) {
                    this.objects[i] = queue.objects[i];
                }
                StdRandom.shuffle(this.objects);
            }
        }

        @Override
        public boolean hasNext() {
            return objects != null && index != objects.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return objects[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<Item>(this);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");
        rq.enqueue("e");
        rq.enqueue("f");
        rq.enqueue("g");

        StdOut.println("RQ size after 7 adds: " + rq.size());
        StdOut.println("Is RQ empty after 7 adds: " + rq.isEmpty());
        StdOut.println("Removed random element: " + rq.dequeue());

        Iterator<String> it1 = rq.iterator();
        Iterator<String> it2 = rq.iterator();

        StdOut.print("Interating with 1st iterator: ");
        while (it1.hasNext()) {
            StdOut.print(it1.next() + ", ");
        }
        StdOut.println();

        StdOut.print("Interating with 2nd iterator: ");
        while (it2.hasNext()) {
            StdOut.print(it2.next() + ", ");
        }
        StdOut.println();

        StdOut.print("Removing items: ");
        while (!rq.isEmpty()) {
            StdOut.print(rq.dequeue() + ", ");
        }
        StdOut.println();

        StdOut.println("RQ size after 6 removes: " + rq.size());
        StdOut.println("Is RQ empty after 6 removes: " + rq.isEmpty());
    }

}
