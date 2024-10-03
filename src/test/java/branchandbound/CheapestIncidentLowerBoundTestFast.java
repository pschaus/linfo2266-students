package branchandbound;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import util.tsp.TSPInstance;

import java.util.List;

import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class CheapestIncidentLowerBoundTestFast {

    public static void checkConstraintsCheapestIncident(double [][] distMatrix, boolean [][] excluded, TSPLowerBoundResult res) {
        int n = distMatrix.length;
        List<Edge> edges = res.edges();
        assertEquals(n, edges.size());
        int tot = 0;
        boolean [] incident = new boolean[n];
        for (Edge e: edges) {
            incident[e.v2()] = true;
            assertEquals(distMatrix[e.v1()][e.v2()], e.cost());
            tot += e.cost();
        }
        for (int i = 0; i < n; i++) {
            assertTrue(incident[i]);
        }
        assertEquals(tot, res.lb());
    }

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

        TSPLowerBoundResult res = new CheapestIncidentLowerBound().compute(distMatrix,excluded);
        checkConstraintsCheapestIncident(distMatrix, excluded, res);
        assertEquals(0, res.lb(), 0.0001);
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

        TSPLowerBoundResult res = new CheapestIncidentLowerBound().compute(distMatrix,excluded);
        checkConstraintsCheapestIncident(distMatrix, excluded, res);
        assertEquals(9, res.lb());

    }


    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "More complex test", on = FAIL)
    public void smallExample() {
        // for a picture of this graph, see data/TSP/graph_debug.svg
        TSPInstance instance = new TSPInstance("data/TSP/graph_debug.xml");
        double [][] distanceMatrix = instance.distanceMatrix;

        boolean [][] excluded  = new boolean[7][7];

        TSPLowerBoundResult res = new CheapestIncidentLowerBound().compute(distanceMatrix,excluded);
        checkConstraintsCheapestIncident(distanceMatrix, excluded, res);
        assertEquals(17, res.lb());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", on = FAIL)
    public void smallExampleOneEdgeExcluded() {
        // for a picture of this graph, see data/TSP/graph_debug.svg
        TSPInstance instance = new TSPInstance("data/TSP/graph_debug.xml");
        double [][] distanceMatrix = instance.distanceMatrix;

        boolean [][] excluded  = new boolean[7][7];
        excluded[1][5] = true;

        TSPLowerBoundResult res = new CheapestIncidentLowerBound().compute(distanceMatrix,excluded);
        checkConstraintsCheapestIncident(distanceMatrix, excluded, res);
        assertEquals(18, res.lb());
    }



}
