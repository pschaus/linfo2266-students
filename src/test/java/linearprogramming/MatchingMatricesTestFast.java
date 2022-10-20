package linearprogramming;


import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
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
    @GradeClass(totalValue = 10, defaultCpuTimeout = 100)
    public static class SimpleTest {

        @Test
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

    }

    private final static int nTest = 5; // number of parametrized run

    @RunWith(Parameterized.class)
    @GradeClass(totalValue = 30, defaultCpuTimeout = 100)
    public static class SmallSizeTest {

        Graph bipartite;
        public SmallSizeTest(Graph bipartite) {
            this.bipartite = bipartite;
        }

        @Parameterized.Parameters
        public static Object[] data() {
            return IntStream.range(0, nTest).mapToObj(i -> Graph.bipartite(66, 66, 72)).toArray();
        }

        @Test
        public void test() {
            Assertions.assertCorrectness(bipartite);
        }

    }

}
