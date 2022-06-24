package ru.vsu.cs.poluecktov.task_2;


public class Task {

    public static boolean isPrime(int n) {
        if (n == 1) {
            return false;
        }
        int i = 2;
        while (i*i <= n) {
            if(n % i == 0) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static void modifyList(DoubleLinkedList<Integer> list) throws DoubleLinkedList.DoubleLinkedListException {
        for(int i = 0; i < list.size(); i++) {
            if(isPrime(list.get(i)) && list.get(i) > 0) {
                if(list.get(i-1) != 0) {
                    list.addBefore(i, 0);
                    list.addAfter(i+1, 0);
                    i+=2;
                } else {
                    list.addAfter(i, 0);
                    i++;
                }
            }
        }
    }

}
