package ru.vsu.cs.poluecktov.task_3;


import java.util.Queue;

public class Task {

    public static void modifyQueueByMe(LinkedListQueue<String> queue) throws Exception {
        int size = queue.count();
        while(size > 0) {
            String current = queue.remove();
            queue.add(current);
            queue.add(current);
            size--;
        }
    }

    public static void modifyQueueStandart(Queue<String> queue) {
        int size = queue.size();
        while(size > 0) {
            String current = queue.remove();
            queue.add(current);
            queue.add(current);
            size--;
        }
    }

}
