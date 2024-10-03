package branchandbound;

import java.util.ArrayList;
import java.util.List;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import util.UF;
import util.tsp.TSPInstance;

import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class OneTreeLowerBoundTestFast {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Sorry, something is wrong with your one-tree algorithm", on = FAIL)
    public void basicOneTree() {
        double[][] distMatrix = new double[][]{
                {0, 1, 1, 0},
                {1, 0, 0, 1},
                {1, 0, 0, 0},
                {0, 1, 0, 0}
        };

        boolean [][] excluded  = new boolean[4][4];

        TSPLowerBoundResult res = new OneTreeLowerBound().compute(distMatrix,excluded);

        List<Edge> edges = res.edges();
        assertTrue(TSPUtil.isOneTree(4,edges));

        assertEquals(1, res.lb(), 0.0001);
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "More complex test on visual example", on = FAIL)
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

        TSPLowerBoundResult res = new OneTreeLowerBound().compute(instance.distanceMatrix, excluded);

        double cost = res.edges().stream().map(Edge::cost).reduce(0.0, Double::sum);
        assertEquals(10, cost, 0.0001);

        assertTrue(TSPUtil.isOneTree(9,res.edges()));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "More complex test", on = FAIL)
    public void smallExample() {
        // for a picture of this graph, see data/TSP/graph_debug.svg
        TSPInstance instance = new TSPInstance("data/TSP/graph_debug.xml");
        double [][] distanceMatrix = instance.distanceMatrix;

        boolean [][] excluded  = new boolean[7][7];

        TSPLowerBoundResult res = new OneTreeLowerBound().compute(distanceMatrix, excluded);

        double cost = res.edges().stream().map(Edge::cost).reduce(0.0, Double::sum);
        assertEquals(22, cost, 0.0001);

        assertTrue(TSPUtil.isOneTree(7,res.edges()));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", on = FAIL)
    public void smallExampleOneEdgeExcluded() {
        // for a picture of this graph, see data/TSP/graph_debug.svg
        TSPInstance instance = new TSPInstance("data/TSP/graph_debug.xml");
        double [][] distanceMatrix = instance.distanceMatrix;

        boolean [][] excluded  = new boolean[7][7];
        excluded[2][5] = true;
        excluded[5][2] = true;

        TSPLowerBoundResult res = new OneTreeLowerBound().compute(distanceMatrix, excluded);

        double cost = res.edges().stream().map(Edge::cost).reduce(0.0, Double::sum);
        assertEquals(23, cost, 0.0001);
        boolean edge25present = edgePresent(2,5,res.edges());
        assertFalse(edge25present);
        assertTrue(TSPUtil.isOneTree(7, res.edges()));
    }


    public boolean edgePresent(int a, int b, List<Edge> edges) {
        for (Edge e: edges) {
            if (e.v1() == a && e.v2() == b) return true;
            if (e.v2() == a && e.v1() == b) return true;
        }
        return false;
    }


}
