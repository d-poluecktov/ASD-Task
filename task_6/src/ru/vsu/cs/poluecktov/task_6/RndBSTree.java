package ru.vsu.cs.poluecktov.task_6;

import java.util.Iterator;
import java.util.Random;

public class RndBSTree<T extends Comparable<? super T>> implements Iterable<T>{


    @Override
    public Iterator<T> iterator() {
        return this.getRoot().iterator();
    }

    public static class Node<T> {
        private T key;
        private int size;
        private Node<T> left;
        private Node<T> right;

        public Node(T key, Node<T> left, Node<T> right) {
            this.key = key;
            this.left = left;
            this.right = right;
        }

        public Node(T key) {
            this.key = key;
            this.left = null;
            this.right = null;
        }

        public T getKey() {
            return key;
        }


        public int getSize() {
            return size;
        }

        public Node<T> getLeft() {
            return left;
        }

        public Node<T> getRight() {
            return right;
        }

        private void fixsize() {
            if(this.left != null && this.right == null) {
                this.size = this.left.size + 1;
            }
            if(this.right != null && this.left == null) {
                this.size = this.right.size + 1;
            }
            if(this.left == null && this.right == null ) {
                this.size = 1;
            }
            if(this.left != null && this.right != null) {
                this.size = this.left.size + this.right.size + 1;
            }
        }

        Iterator<T> iterator() {
            return BinaryTreeAlgorithms.inOrderValues(this).iterator();
        }
    }

    public RndBSTree(T key) {
        this.root = new Node<>(key);
    }

    public RndBSTree() {
    }

    protected Node<T> root = null;
    private int size = 0;

    public Node<T> getRoot() {
        return root;
    }

    public T get(T key) throws Exception {
        class Inner {
            Node<T> getNodeFromRoot(Node<T> node, T key) throws Exception {
                if (node == null) {
                    throw new Exception("NoSuchElementInTree");
                }
                int cmp = node.getKey().compareTo(key);

                if(cmp == 0) {
                    return node;
                } else if (cmp > 0) {
                    return getNodeFromRoot(node.getLeft(), key);
                } else {
                    return getNodeFromRoot(node.getRight(), key);
                }


            }
        }
        return new Inner().getNodeFromRoot(getRoot(), key).key;
    }



    public int size() {
        return size;
    }

    public T put(T key) {
        class Inner {
            Node<T> addNode(Node<T> node, T key) {
                if(node == null) {
                    size++;
                    return new Node<>(key);
                }
                Random random = new Random();
                if(random.nextInt(node.size + 1) == 0) {
                    node = insertRoot(node, key);
                    node.fixsize();
                    return node;
                }
                int cmp = node.getKey().compareTo(key);
                if(cmp == 0) {
                    node.key = key;
                } else {
                    if (cmp > 0) {
                        node.left = addNode(node.left, key);
                    } else {
                        node.right = addNode(node.right, key);
                    }
                }
                node.fixsize();
                return node;
            }
        }
        if(root == null) {
            root = new Node<>(key);
            root.fixsize();
            size++;
            return root.key;
        } else {
            root = new Inner().addNode(root, key);
            root.fixsize();
            return root.key;
        }
    }

    private Node<T> rotateRight(Node<T> p) {
        Node q = p.left;
        if(q == null) {
            return p;
        }
        p.left = q.right;
        q.right = p;
        q.size = p.size;
        p.fixsize();
        return q;
    }

    private Node<T> rotateLeft(Node<T> q) {
        Node p = q.right;
        if(p == null) {
            return q;
        }
        q.right = p.left;
        p.left = q;
        p.size = q.size;
        q.fixsize();
        return p;
    }

    private Node<T> insertRoot(Node<T> node,T key) {
        if(node == null) {
            size++;
            Node newNode = new Node<>(key);
            newNode.fixsize();
            return newNode;
        }
        int cmp = key.compareTo(node.key);
        if(cmp == 0) {
            node.key = key;
            return node;
        } else if(cmp < 0) {
            node.left = insertRoot(node.left, key);
            return rotateRight(node);
        } else {
            node.right = insertRoot(node.right, key);
            return rotateLeft(node);
        }
    }

    private Node<T> join(Node<T> p, Node<T> q) {
        if(p == null) {
            return q;
        }
        if(q == null) {
            return  p;
        }
        Random random = new Random();
        if(random.nextInt(p.size + q.size) < p.size) {
            p.right = join(p.right, q);
            p.fixsize();
            return p;
        } else {
            q.left = join(p, q.left);
            q.fixsize();
            return q;
        }
    }

    public T remove(T key) {
        class Inner {
            Node<T> removeFromRoot(Node<T> p, T key) {
                if(p == null) {
                    return p;
                }
                int cmp = key.compareTo(p.key);
                if(cmp == 0) {
                    Node<T> q = join(p.left, p.right);
                    size--;
                    return q;
                } else if(cmp < 0) {
                    p.left = removeFromRoot(p.left, key);
                } else {
                    p.right = removeFromRoot(p.right, key);
                }
                return p;
            }
        }
        Inner inner = new Inner();
        root = inner.removeFromRoot(root, key);
        return key;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean contains(T key) {
        class Inner {
            boolean containsFromRoot(Node<T> node, T key) {
                if(node == null) {
                    return false;
                }
                int cmp = key.compareTo(node.key);
                if(cmp == 0) {
                    return true;
                } else if(cmp < 0) {
                    return containsFromRoot(node.left, key);
                } else {
                    return containsFromRoot(node.right, key);
                }
            }
        }
        Inner inner = new Inner();
        return inner.containsFromRoot(root, key);
    }


}
