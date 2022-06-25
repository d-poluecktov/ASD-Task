package ru.vsu.cs.poluecktov.task_6;

/**
 * Реализация словаря на базе простого (наивного) дерева поиска
 *
 * @param <K>
 * @param <V>
 */
public class RndBSTreeMap<K extends Comparable<K>, V> implements BSTreeMap<K, V> {

    private final RndBSTree<MapTreeEntry<K, V>> tree = new RndBSTree<>();

    @Override
    public RndBSTree<MapTreeEntry<K, V>> getTree() {
        return tree;
    }
}
