package branchandbound;

import java.util.List;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import org.junit.Test;
import util.UF;
import util.tsp.TSPInstance;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


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
        checkOneTree(4,edges);

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

        checkOneTree(9,res.edges());
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", onFail = true)
    public void smallExample() {
        // see here for the graph https://pasteboard.co/iVjXKUlNE6ev.png
        double [][] distanceMatrix = new double[7][7];
        List<Edge> edges = List.of(
                new Edge(0,1,4),
                new Edge(0,5,5),
                new Edge(0,2,2),
                new Edge(1,6,7),
                new Edge(1,5,1),
                new Edge(2,5,4),
                new Edge(2,4,7),
                new Edge(2,3,3),
                new Edge(3,4,5),
                new Edge(4,5,2),
                new Edge(4,6,8),
                new Edge(5,6,6));

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

        checkOneTree(7,res.edges());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", onFail = true)
    public void smallExampleOneEdgeExcluded() {
        // see here for the graph https://pasteboard.co/iVjXKUlNE6ev.png
        double [][] distanceMatrix = new double[7][7];
        List<Edge> edges = List.of(
                new Edge(0,1,4),
                new Edge(0,5,5),
                new Edge(0,2,2),
                new Edge(1,6,7),
                new Edge(1,5,1),
                new Edge(2,5,4),
                new Edge(2,4,7),
                new Edge(2,3,3),
                new Edge(3,4,5),
                new Edge(4,5,2),
                new Edge(4,6,8),
                new Edge(5,6,6));

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
        checkOneTree(7,res.edges());
    }


    public boolean edgePresent(int a, int b, List<Edge> edges) {
        for (Edge e: edges) {
            if (e.v1() == a && e.v2() == b) return true;
            if (e.v2() == a && e.v1() == b) return true;
        }
        return false;
    }


    public void checkOneTree(int n, List<Edge> edges) {
        int [] degree = new int[n];
        UF uf = new UF(n);
        for (Edge e: edges) {
            uf.union(e.v1(),e.v2());
            degree[e.v1()] += 1;
            degree[e.v2()] += 1;
        }
        assertEquals(n,edges.size());
        assertEquals(2,degree[0]);
        assertEquals(1, uf.count());
    }


}
