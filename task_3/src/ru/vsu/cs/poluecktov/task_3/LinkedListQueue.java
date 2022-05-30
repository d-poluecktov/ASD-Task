package ru.vsu.cs.poluecktov.task_3;

/*Дана очередь из строк. Обработать очередь таким образом, чтобы каждый элемент
 очереди был продублирован (например: { "stack", "list", "queue" } –> { "stack", "stack",
 "list", "list", "queue", "queue" }).*/


public class LinkedListQueue<T> extends DoubleLinkedList<T> implements SimpleQueue<T> {
    @Override
    public void add(T value) {
        this.addLast(value);
    }

    @Override
    public T element() throws Exception {
        if (this.empty()) {
            throw new Exception("Queue is empty");
        }
        return this.getFirst();
    }

    @Override
    public T remove() throws Exception {
        T result = this.element();
        this.removeFirst();
        return result;
    }


    @Override
    public int count() {
        return this.size();
    }

    @Override
    public boolean empty() {
        return this.count() == 0;
    }


    public static String[] toArray(LinkedListQueue<String> queue) {
        int count = queue.count();
        String[] strings = new String[count];
        int i = 0;
        for (String string: queue) {
            strings[i] = string;
            i++;
        }
        return strings;


    }


}
