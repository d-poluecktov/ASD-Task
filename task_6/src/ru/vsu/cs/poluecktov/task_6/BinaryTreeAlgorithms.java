package ru.vsu.cs.poluecktov.task_6;

import java.util.Iterator;
import java.util.Stack;

public class BinaryTreeAlgorithms {



    /**
     * Обход поддерева с вершиной в данном узле в виде итератора в
     * симметричном/поперечном/центрированном/LNR порядке (предполагается, что в
     * процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> inOrderValues(RndBSTree.Node<T> treeNode) {
        return () -> {
            Stack<RndBSTree.Node<T>> stack = new Stack<>();
            RndBSTree.Node<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    RndBSTree.Node<T> node = stack.pop();
                    T result = node.getKey();
                    if (node.getRight() != null) {
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node);
                            node = node.getLeft();
                        }
                    }
                    return result;
                }
            };
        };
    }

}
