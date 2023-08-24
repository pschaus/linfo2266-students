package linearprogramming;

import org.javagrader.Grade;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class MatchingMatricesTestFast {

    /**
     * Instance tested:
     *
     *   0     1--+  2--+  3
     *   |     |  |     |  |
     *   4     5  +--6  +--7
     *
     * The objective value should be 3
     */
    @Grade(value = 10, cpuTimeout = 100, unit = MILLISECONDS)
    public void simpleTest() {
        Edge[] edges = new Edge[] {
                new Edge(0, 4),
                new Edge(1, 5),
                new Edge(1, 6),
                new Edge(2, 7),
                new Edge(3, 7)
        };
        Graph graph = Graph.bipartite(8, edges);
        try {
            MatchingMatrices matching = new MatchingMatrices(graph);
            LinearProgramming simplex = new LinearProgramming(matching.A, matching.b, matching.c);
            int nEdges = (int) simplex.value();
            assertEquals(3, nEdges);
            double[] primal = simplex.primal();
            assertTrue(matching.isEdgeSelected(primal, edges[0]));
            // either (1-5) is selected or (1-6) is selected
            assertTrue(matching.isEdgeSelected(primal, edges[1]) ^ matching.isEdgeSelected(primal, edges[2]));
            // either (2-7) is selected or (3-7) is selected
            assertTrue(matching.isEdgeSelected(primal, edges[3]) ^ matching.isEdgeSelected(primal, edges[4]));
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    public static List<Arguments> getGraphParams() {
        int nVertices = 66;
        int nEdges = 72;
        return IntStream.range(0, 5).mapToObj(i -> arguments(named("n" + nVertices + "_e" + nEdges + "_" + i, nVertices), nEdges)).collect(Collectors.toList());
    }

    @Grade(value = 30, cpuTimeout = 100, unit = MILLISECONDS)
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getGraphParams")
    public void smallSizeTest(int vertices, int edges) {
        Graph bipartite = Graph.bipartite(vertices, vertices, edges);
        Assertions.assertCorrectness(bipartite);
    }

}
