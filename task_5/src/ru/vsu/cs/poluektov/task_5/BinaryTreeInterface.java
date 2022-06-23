package ru.vsu.cs.poluektov.task_5;

import java.awt.Color;
import java.util.Iterator;

/**
 * Интерфейс для двоичного дерева с реализацией по умолчанию различных обходов
 * дерева
 *
 * @param <T>
 */
public interface BinaryTreeInterface<T> extends Iterable<T> {

    /**
     * Интерфейс для описания узла двоичного дерева
     *
     * @param <T>
     */
    interface TreeNode<T> extends Iterable<T> {

        /**
         * @return Значение в узле дерева
         */
        T getValue();

        /**
         * @return Левое поддерево
         */
        default TreeNode<T> getLeft() {
            return null;
        }

        /**
         * @return Правое поддерево
         */
        default TreeNode<T> getRight() {
            return null;
        }

        /**
         * @return Цвет узла (для рисования и не только)
         */
        default Color getColor() {
            return Color.BLACK;
        }

        /**
         * Реализация Iterable&lt;T&gt;
         *
         * @return Итератор
         */
        @Override
        default Iterator<T> iterator() {
            return BinaryTreeAlgorithms.inOrderValues(this).iterator();
        }

        /**
         * Представление поддерева в виде строки в скобочной нотации
         *
         * @return дерево в виде строки
         */
        default String toBracketStr() {
            return BinaryTreeAlgorithms.toBracketStr(this);
        }
    }

    /**
     * @return Корень (вершина) дерева
     */
    TreeNode<T> getRoot();


    /**
     * Реализация Iterable&lt;T&gt;
     *
     * @return Итератор
     */
    @Override
    default Iterator<T> iterator() {
        return this.getRoot().iterator();
    }


    /**
     * Представление дерева в виде строки в скобочной нотации
     *
     * @return дерево в виде строки
     */
    default String toBracketStr() {
        return this.getRoot().toBracketStr();
    }
}
