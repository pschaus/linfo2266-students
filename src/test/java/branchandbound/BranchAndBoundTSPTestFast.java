package branchandbound;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.tsp.TSPInstance;

import java.util.List;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("fast")
@Grade
public class BranchAndBoundTSPTestFast {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Sorry, TSP not working yet", on = FAIL)
    public void readableTestToDebug() {
        // https://pasteboard.co/AWbeWgMG188K.png (use this link to visualize the instance and debug)
        int [] xCoord = new int[] {10,10,20,70,40,60,50};
        int [] yCoord = new int[] {10,30,60,40,10,20,60};
        TSPInstance tsp = new TSPInstance(xCoord,yCoord);
        List<Edge> edges;

        boolean [][] expectedEdges = new boolean[7][7];
        // 0-1, 1-2, 2-6, 6-3, 3-5, 5-4, 4-0
        expectedEdges[0][1] = expectedEdges[1][0] = true;
        expectedEdges[1][2] = expectedEdges[2][1] = true;
        expectedEdges[2][6] = expectedEdges[6][2] = true;
        expectedEdges[6][3] = expectedEdges[3][6] = true;
        expectedEdges[3][5] = expectedEdges[5][3] = true;
        expectedEdges[5][4] = expectedEdges[4][5] = true;
        expectedEdges[4][0] = expectedEdges[0][4] = true;

        OneTreeLowerBound [] algos = new OneTreeLowerBound[] {new SimpleOneTree(), new HeldKarpOneTree()};

        for (OneTreeLowerBound algo: algos) {
            edges = BranchAndBoundTSP.optimize(tsp,algo);
            assertEquals(7, edges.size());
            boolean [][] foundEdges = new boolean[7][7];
            for (Edge e: edges) {
                foundEdges[e.v1()][e.v2()] = foundEdges[e.v2()][e.v1()] = true;
            }
            for (int i = 0; i < expectedEdges.length; i++) {
                for (int j = 0; j < expectedEdges.length; j++) {
                    assertEquals(expectedEdges[i][j], foundEdges[i][j]);
                }
            }
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Comparison Two Bound on random tests", on = FAIL)
    public void randomTestToDebug() {

        for (int seed = 0; seed < 100; seed++) {

            TSPInstance tsp = new TSPInstance(4, seed, 100);

            List<Edge> edges1 = BranchAndBoundTSP.optimize(tsp, new SimpleOneTree());
            int tot1 = edges1.stream().map(Edge::cost).mapToInt(e -> (int) Math.rint(e)).sum();

            List<Edge> edges2 = BranchAndBoundTSP.optimize(tsp, new HeldKarpOneTree());
            int tot2 = edges2.stream().map(Edge::cost).mapToInt(e -> (int) Math.rint(e)).sum();
            assertEquals(tot1, tot2);
        }
    }

    public static List<Arguments> getTSPInstances() {
        return BranchAndBoundTSPTest.readTSPInstances(8);
    }

    public static void testSolvingOptimality(TSPInstance instance, OneTreeLowerBound algo) {
        List<Edge> edges = BranchAndBoundTSP.optimize(instance,algo);
        int objective = instance.objective;
        double tourLength = 0;
        for (Edge e: edges) {
            tourLength += instance.distanceMatrix[e.v1()][e.v2()];
        }
        assertEquals(objective,tourLength, 0.001);
    }

    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances")
    public void testOneTree(TSPInstance instance) {
        testSolvingOptimality(instance, new SimpleOneTree());
    }

    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances")
    public void testHeldKarp(TSPInstance instance) {
        testSolvingOptimality(instance, new HeldKarpOneTree());
    }

}
