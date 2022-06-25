package ru.vsu.cs.poluecktov.task_7.Program;

import java.util.*;

/**
 * Реализация графа на основе матрицы смежности
 */
public class AdjMatrixGraph implements Graph {

    private boolean[][] adjMatrix = null;
    private int vCount = 0;
    private int eCount = 0;
    List<Set<Integer>> colorGroups = new ArrayList<>();

    /**
     * Конструктор
     * @param vertexCount Кол-во вершин графа (может увеличиваться при добавлении ребер)
     */
    public AdjMatrixGraph(int vertexCount) {
        adjMatrix = new boolean[vertexCount][vertexCount];
        vCount = vertexCount;
    }

    /**
     * Конструктор без парметров
     * (лучше не использовать, т.к. при добавлении вершин каждый раз пересоздается матрица)
     */
    public AdjMatrixGraph() {
        this(0);
    }

    @Override
    public int vertexCount() {
        return vCount;
    }

    @Override
    public int edgeCount() {
        return eCount;
    }

    @Override
    public void addEdge(int v1, int v2) {
        int maxV = Math.max(v1, v2);
        if (maxV >= vertexCount()) {
            adjMatrix = Arrays.copyOf(adjMatrix, maxV + 1);
            for (int i = 0; i <= maxV; i++) {
                adjMatrix[i] = i < vCount ? Arrays.copyOf(adjMatrix[i], maxV + 1) : new boolean[maxV + 1];
            }
            vCount = maxV + 1;
        }
        if (!adjMatrix[v1][v2]) {
            adjMatrix[v1][v2] = true;
            eCount++;
            // для наследников
            if (!(this instanceof Digraph)) {
                adjMatrix[v2][v1] = true;
            }
        }
    }

    @Override
    public void removeEdge(int v1, int v2) {
        if (adjMatrix[v1][v2]) {
            adjMatrix[v1][v2] = false;
            eCount--;
            // для наследников
            if (!(this instanceof Digraph)) {
                adjMatrix[v2][v1] = false;
            }
        }
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        return new Iterable<Integer>() {
            Integer nextAdj = null;

            @Override
            public Iterator<Integer> iterator() {
                for (int i = 0; i < vCount; i++) {
                    if (adjMatrix[v][i]) {
                        nextAdj = i;
                        break;
                    }
                }

                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return nextAdj != null;
                    }

                    @Override
                    public Integer next() {
                        Integer result = nextAdj;
                        nextAdj = null;
                        for (int i = result + 1; i < vCount; i++) {
                            if (adjMatrix[v][i]) {
                                nextAdj = i;
                                break;
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }

    public void getColorGroups() {
        boolean[][] mask = new boolean[adjMatrix.length][adjMatrix.length];
        for(int i = 0; i < mask.length; i++) {
            for(int j = 0; j < mask[i].length; j++) {
                if(i == j) {
                    mask[i][j] = true;
                } else {
                    mask[i][j] = adjMatrix[i][j];
                }
            }
        }
        int count = 0;
        for (int i = 0; i < mask.length; i++) {
            if(!hasFalse(mask[i])) {
                colorGroups.add(new HashSet<>());
                count++;
                colorGroups.get(count - 1).add(i);
            }
        }
        for(int i = 0; i < mask.length; i++) {
            if(!containsInColorGroups(i)) {
                colorGroups.add(new HashSet<>());
                count++;
                while (hasFalse(mask[i])) {
                    int j = getFirstFalseIndex(mask[i]);
                    boolean[] firstSummand = mask[i];
                    boolean[] secondSummand = mask[j];
                    mask[i] = sumBooleanArrays(firstSummand, secondSummand);
                    if(!containsInColorGroups(i)) {
                        colorGroups.get(count - 1).add(i);
                    }
                    if(!containsInColorGroups(j)) {
                        colorGroups.get(count - 1).add(j);
                    }
                }
            }
        }

    }

    public int[] getColors() {
        getColorGroups();
        int[] colors = new int[vCount];
        int i = 0;
        while(i < adjMatrix.length) {
            for (int j = 0; j < colorGroups.size(); j++) {
                if(colorGroups.get(j).contains(i)) {
                    colors[i] = j;
                    i++;
                    break;
                }
            }
        }
        return colors;
    }

    private boolean hasFalse(boolean[] arr) {
        for (boolean val: arr) {
            if(!val) {
                return true;
            }
        }
        return false;
    }

    private int getFirstFalseIndex(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if(!arr[i]) {
                return i;
            }
        }
        return 0;
    }

    private boolean containsInColorGroups(int index) {
        if(colorGroups.size() == 0) {
            return false;
        } else {
            for (Set<Integer> group: colorGroups) {
                if(group.contains(index)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean[] sumBooleanArrays(boolean[] firstSummand, boolean[] secondSummand) {
        boolean[] result = new boolean[firstSummand.length];
        for(int i = 0; i < firstSummand.length; i++) {
            result[i] = firstSummand[i] || secondSummand[i];
        }
        return result;
    }

    // Перегрузка для быстродействия
    @Override
    public boolean isAdj(int v1, int v2) {
        return adjMatrix[v1][v2];
    }
}
