package ru.vsu.cs.poluecktov.task_3;

import java.util.Iterator;

public class DoubleLinkedList<T> implements Iterable<T>{

    public static class DoubleLinkedListException extends Exception {
        public DoubleLinkedListException(String message) {
            super(message);
        }
    }

    private class ListNode<T> {
        public T value;
        public ListNode next;
        public ListNode prev;

        public ListNode(T value) {
            this.next = null;
            this.prev = null;
            this.value = value;
        }

        public ListNode(ListNode<T> prev, T value, ListNode<T> next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }

    }

    private ListNode<T> head = null;
    private ListNode<T> tail = null;
    private int size = 0;

    public int size() {
        return size;
    }

    private void checkEmptyError() throws DoubleLinkedListException {
        if (size == 0) {
            throw new DoubleLinkedListException("Список пустой");
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T value) {
        if(isEmpty()) {
            head = tail = new ListNode<T>(value);
        } else {
            ListNode<T> current = head;
            head = new ListNode<T>(null, value, current);
            head.next.prev = head;
        }
        size++;
    }

    public void addLast(T value) {
        if(isEmpty()) {
            head = tail = new ListNode<T>(value);
        } else {
            tail.next = new ListNode<T>(value);
            tail.next.prev = tail;
            tail = tail.next;
        }
        size++;
    }



    public void addBefore(int index, T value) throws DoubleLinkedListException {
        checkEmptyError();
        ListNode<T> current = getNode(index);

        ListNode<T> listNode = new ListNode<T>(current.prev, value, current);
        if(current.prev != null) {
            current.prev.next = listNode;
        } else {
            head = listNode;
        }
        current.prev = listNode;
        size++;
    }

    public void addAfter(int index, T value) throws DoubleLinkedListException {
        checkEmptyError();
        ListNode<T> current = getNode(index);

        ListNode<T> listNode = new ListNode<T>(current, value, current.next);
        if(current.next != null) {
            current.next.prev = listNode;
        }
        current.next = listNode;
        size++;
    }

    public void remove(int index) throws DoubleLinkedListException {
        checkEmptyError();
        if (index < 0 || index >= size) {
            throw new DoubleLinkedListException("Неверный индекс");
        }
        if (index == 0) {
            removeFirst();
            return;
        } if (index == size() - 1) {
            removeLast();
            return;
        } else {
            ListNode prev = getNode(index - 1);
            prev.next = prev.next.next;
            prev.next.prev = prev;
            size--;
        }
    }

    public void removeFirst() throws DoubleLinkedListException {
        checkEmptyError();
        head = head.next;
        if (size == 1) {
            head = tail = null;
        }
        size--;

    }
    public void removeLast() throws DoubleLinkedListException {
        checkEmptyError();
        if (size == 1) {
            head = tail = null;
        } else {
            tail = getNode(size - 2);
            tail.next = null;
        }
        size--;

    }

    private ListNode<T> getNode(int index) {
        ListNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    public T getFirst() throws DoubleLinkedListException {
        checkEmptyError();
        return head.value;
    }

    public T getLast() throws DoubleLinkedListException {
        checkEmptyError();
        return getNode(size()-1).value;
    }

    public T get(int index) {
        return getNode(index).value;
    }

    @Override
    public Iterator<T> iterator() {
        class SimpleLinkedListIterator implements Iterator<T> {
            ListNode<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T value = current.value;
                current = current.next;
                return value;
            }
        }

        return new SimpleLinkedListIterator();
    }
}
