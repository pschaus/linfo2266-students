package branchandbound;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import util.UF;
import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.List;

import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("fast")
@Grade
public class HeldKarpLowerBoundFast {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "More complex test", on = FAIL)
    public void smallExample() {
        // for a picture of this graph, see data/TSP/graph_debug.svg
        TSPInstance instance = new TSPInstance("data/TSP/graph_debug.xml");
        double [][] distanceMatrix = instance.distanceMatrix;

        boolean [][] excluded  = new boolean[7][7];

        TSPLowerBoundResult res = new HeldKarpLowerBound().compute(distanceMatrix, excluded);

        assertEquals(29, res.lb(), 0.1);

        checkOneTree(7,res.edges());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
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
