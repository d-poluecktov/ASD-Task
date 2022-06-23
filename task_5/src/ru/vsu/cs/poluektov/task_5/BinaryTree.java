package ru.vsu.cs.poluektov.task_5;

import java.util.*;
import java.util.function.Function;

/**
 * Реализация простейшего бинарного дерева
 */
public class BinaryTree<T> implements BinaryTreeInterface<T> {

    protected class TreeNode<T> implements BinaryTreeInterface.TreeNode<T> {
        public T value;
        public TreeNode<T> left;
        public TreeNode<T> right;
        public boolean visited = false;

        public TreeNode(T value, TreeNode<T> left, TreeNode<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public TreeNode(T value) {
            this(value, null, null);
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public TreeNode<T> getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            TreeNode<?> treeNode = (TreeNode<?>) o;
            if(treeNode != null) {
                return Objects.equals(value, treeNode.value) && ((Objects.equals(left, treeNode.left) && Objects.equals(right, treeNode.right)) ||
                        (Objects.equals(right, treeNode.left) && Objects.equals(left, treeNode.right)));
            } else {
                return false;
            }

        }


    }

    protected TreeNode root = null;

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public BinaryTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public BinaryTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, Object::toString);
    }

    public BinaryTree() {
        this(null);
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    public void clear() {
        root = null;
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private static class IndexWrapper {
        public int index = 0;
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        // пропуcкаем возможные пробелы
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
        )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private TreeNode fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        TreeNode parentNode = new TreeNode(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',') {
                parentNode.left = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                parentNode.right = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        TreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }



    public boolean treeEquals(BinaryTree<T> secondTree) {
        Queue<TreeNode> firstTreeQueue = new LinkedList<>();
        TreeNode<T> firstRoot = root;
        firstRoot.visited = true;
        firstTreeQueue.add(firstRoot);

        Queue<TreeNode> secondTreeQueue = new LinkedList<>();
        TreeNode<T> secondRoot = secondTree.getRoot();
        secondRoot.visited = true;
        secondTreeQueue.add(secondRoot);

        boolean flag = true;
        if(firstTreeQueue.size() == secondTreeQueue.size()) {
            while (flag) {
                TreeNode<T> firstCurrent = firstTreeQueue.poll();
                TreeNode<T> secondCurrent = secondTreeQueue.poll();

                if (firstCurrent.value == secondCurrent.value) {
                    if (firstCurrent.left != null) {
                        firstTreeQueue.add(firstCurrent.left);
                    }
                    if (firstCurrent.right != null) {
                        firstTreeQueue.add(firstCurrent.right);
                    }
                    if (secondCurrent.left != null) {
                        secondTreeQueue.add(secondCurrent.left);
                    }
                    if (secondCurrent.right != null) {
                        secondTreeQueue.add(secondCurrent.right);
                    }
                } else {
                    return false;
                }

                if (firstTreeQueue.size() != secondTreeQueue.size()) {
                    return false;
                }

                if(firstTreeQueue.size() == 0 && secondTreeQueue.size() == 0) {
                    flag = false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

}
