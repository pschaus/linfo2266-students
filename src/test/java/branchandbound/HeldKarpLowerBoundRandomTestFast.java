package branchandbound;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.UF;
import util.tsp.TSPInstance;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("fast")
@Grade
public class HeldKarpLowerBoundRandomTestFast {

    @Grade(value = 5, cpuTimeout = 4)
    @GradeFeedback(message = "Sorry, something is wrong cannot find the correct one tree", on = FAIL)
    @GradeFeedback(message = "Your one-tree algo is too slow", on = TIMEOUT)
    @ParameterizedTest(name = "[{index}] n5 - seed {0}")
    @MethodSource("getSeeds")
    public void testBruteForceVsSimpleOneTree(int seed) {
        TSPInstance instance = new TSPInstance(5,seed,100);
        boolean [][] excluded = new boolean[instance.n][instance.n];
        excluded[2][3] = true;
        excluded[3][2] = true;
        excluded[4][1] = true;
        excluded[1][4] = true;

        TSPLowerBoundResult r1 = bruteForceOneTree(instance.distanceMatrix, excluded);
        TSPLowerBoundResult r2 = new HeldKarpLowerBound().compute(instance.distanceMatrix, excluded);

        assertTrue(r2.lb() >= r1.lb()+1); // held-karp lb should be stronger
        assertTrue(TSPUtil.isOneTree(instance.n,r2.edges()));
        assertTrue(TSPUtil.isOneTree(instance.n,r1.edges()));
        assertFalse(edgePresent(2,3,r2.edges()));
        assertFalse(edgePresent(2,3,r1.edges()));
    }

    public static List<Integer> getSeeds() {
        return IntStream.range(20, 40).boxed().collect(Collectors.toList());
    }

    private static TSPLowerBoundResult bruteForceOneTree(double [][] dist, boolean[][] excluded) {
        Set<Edge> edges = new HashSet<>();
        for (int i = 0; i < dist.length; i++) {
            for (int j = i+1; j < dist.length; j++) {
                Edge e = new Edge(i,j,dist[i][j]);
                edges.add(e);
            }
        }
        double bestOneTreeCost = Double.MAX_VALUE;
        Set<Edge> bestOneTree = null;
        // enumerate all power-set of edges
        for (Set<Edge> candidate: TSPUtil.powerSet(edges,dist.length)) {
            if (TSPUtil.isOneTree(dist.length, candidate)  && candidate.stream().noneMatch(e -> excluded[e.v1()][e.v2()])) {
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
        return new TSPLowerBoundResult(bestOneTreeCost, new ArrayList<>(bestOneTree));
    }





    public static boolean edgePresent(int a, int b, List<Edge> edges) {
        for (Edge e: edges) {
            if (e.v1() == a && e.v2() == b) return true;
            if (e.v2() == a && e.v1() == b) return true;
        }
        return false;
    }

}
