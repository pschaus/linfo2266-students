package branchandbound;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import org.junit.Test;
import util.UF;
import util.tsp.TSPInstance;

import static org.junit.Assert.*;


public class SimpleOneTreeTestFast {


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "Sorry, something is wrong with your one-tree algorithm", onFail = true)
    public void basicOneTree() {
        double[][] distMatrix = new double[][]{
                {0, 1, 1, 0},
                {1, 0, 0, 1},
                {1, 0, 0, 0},
                {0, 1, 0, 0}
        };

        boolean [][] excluded  = new boolean[4][4];

        OneTreeResult res = new SimpleOneTree().compute(distMatrix,excluded);


        List<Edge> edges = res.edges();
        assertTrue(isOneTree(4,edges));

        assertEquals(1, res.lb(), 0.0001);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test on visual example", onFail = true)
    public void visualExample() {
        //  0 --- 1 --- 2 --- 3
        //  |                 |
        //  |                 4
        //  |                 |
        //  8 --- 7 --- 6 --- 8

        int [] xCoord = new int[] {0,0,0,0,1,2,2,2,2};
        int [] yCoord = new int[] {0,1,2,3,3,3,2,1,0};

        TSPInstance instance = new TSPInstance(xCoord,yCoord);
        double [][] distMatrix = instance.distanceMatrix;
        boolean [][] excluded  = new boolean[distMatrix.length][distMatrix.length];

        OneTreeResult res = new SimpleOneTree().compute(instance.distanceMatrix, excluded);

        double cost = res.edges().stream().map(e -> e.cost()).reduce(0.0, (a,b) -> a+b);
        assertEquals(10, cost, 0.0001);

        assertTrue(isOneTree(9,res.edges()));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", onFail = true)
    public void smallExample() {
        // see here for the graph https://pasteboard.co/iVjXKUlNE6ev.png
        double [][] distanceMatrix = new double[7][7];
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 5, 5));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(1, 6, 7));
        edges.add(new Edge(1, 5, 1));
        edges.add(new Edge(2, 5, 4));
        edges.add(new Edge(2, 4, 7));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(3, 4, 5));
        edges.add(new Edge(4, 5, 2));
        edges.add(new Edge(4, 6, 8));
        edges.add(new Edge(5, 6, 6));

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
            }
        }
        for (Edge e: edges) {
            distanceMatrix[e.v1()][e.v2()] = e.cost();
            distanceMatrix[e.v2()][e.v1()] = e.cost();
        }

        boolean [][] excluded  = new boolean[7][7];

        OneTreeResult res = new SimpleOneTree().compute(distanceMatrix, excluded);

        double cost = res.edges().stream().map(e -> e.cost()).reduce(0.0, (a,b) -> a+b);
        assertEquals(22, cost, 0.0001);

        assertTrue(isOneTree(7,res.edges()));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", onFail = true)
    public void smallExampleOneEdgeExcluded() {
        // see here for the graph https://pasteboard.co/iVjXKUlNE6ev.png
        double [][] distanceMatrix = new double[7][7];
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 5, 5));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(1, 6, 7));
        edges.add(new Edge(1, 5, 1));
        edges.add(new Edge(2, 5, 4));
        edges.add(new Edge(2, 4, 7));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(3, 4, 5));
        edges.add(new Edge(4, 5, 2));
        edges.add(new Edge(4, 6, 8));
        edges.add(new Edge(5, 6, 6));

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
            }
        }
        for (Edge e: edges) {
            distanceMatrix[e.v1()][e.v2()] = e.cost();
            distanceMatrix[e.v2()][e.v1()] = e.cost();
        }

        boolean [][] excluded  = new boolean[7][7];
        excluded[2][5] = true;
        excluded[5][2] = true;

        OneTreeResult res = new SimpleOneTree().compute(distanceMatrix, excluded);

        double cost = res.edges().stream().map(e -> e.cost()).reduce(0.0, (a,b) -> a+b);
        assertEquals(23, cost, 0.0001);
        boolean edge25present = edgePresent(2,5,res.edges());
        assertFalse(edge25present);
        assertTrue(isOneTree(7, res.edges()));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "Random test full enumeration", onFail = true)
    public void randomTest() {
        for (int seed = 0; seed < 200; seed++) {
            TSPInstance instance = new TSPInstance(5,seed,100);
            double [][] dist = instance.distanceMatrix;

            // edge 1-2 is excluded
            boolean [][] excluded = new boolean[5][5];
            excluded[1][2] = true;
            excluded[2][1] = true;
            OneTreeResult res = new SimpleOneTree().compute(dist,excluded);

            Set<Edge> edges = new HashSet<>();
            Edge forbidden = null;
            for (int i = 0; i < 5; i++) {
                for (int j = i+1; j < 5; j++) {
                    Edge e = new Edge(i,j,dist[i][j]);
                    edges.add(e);
                    if (i == 1 && j == 2) {
                        forbidden = e;
                    }

                }
            }
            double bestOneTreeCost = Double.MAX_VALUE;
            Set<Edge> bestOneTree = null;
            // enumerate all power-set of edges
            for (Set<Edge> candidate: powerSet(edges)) {
                if (isOneTree(5,candidate) && !candidate.contains(forbidden)) {
                    double cost = 0;
                    for (Edge e: candidate) {
                        cost += e.cost();
                    }
                    if (cost < bestOneTreeCost) {
                        bestOneTreeCost = cost;
                        bestOneTree = candidate;
                    }
                }
            }
            assertEquals(res.lb(),bestOneTreeCost,0.01);
        }
    }


    public boolean edgePresent(int a, int b, List<Edge> edges) {
        for (Edge e: edges) {
            if (e.v1() == a && e.v2() == b) return true;
            if (e.v2() == a && e.v1() == b) return true;
        }
        return false;
    }


    public boolean isOneTree(int n, Iterable<Edge> edges) {
        int [] degree = new int[n];
        UF uf = new UF(n);
        int size = 0;
        for (Edge e: edges) {
            uf.union(e.v1(),e.v2());
            degree[e.v1()] += 1;
            degree[e.v2()] += 1;
            size++;
        }
        for (Edge e: edges) {
            if (e.v1() == 0 && degree[e.v2()] < 2) {
                return false;
            }
            if (e.v2() == 0 && degree[e.v1()] < 2) {
                return false;
            }
        }
        if (n != size) return false;
        if (2 != degree[0]) return false;
        if (1 != uf.count()) return false;
        return true;
    }


    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }


}
