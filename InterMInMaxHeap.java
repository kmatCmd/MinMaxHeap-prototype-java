package minMaxHeap.lib.base;

public interface InterMInMaxHeap<E> {
    boolean insert(E e);
    E getMax();
    E getMin();
    E getByIndex(int index);
    E getByRandom();
    boolean isEmpty();
    boolean isFull();
    int size();
    E peekMin();
    E peekMax();
    E peekByIndex(int index);
}
