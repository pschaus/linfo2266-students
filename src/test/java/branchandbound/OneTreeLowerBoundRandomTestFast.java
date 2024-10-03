package branchandbound;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.UF;
import util.tsp.TSPInstance;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class OneTreeLowerBoundRandomTestFast {

    @Grade(value = 5, cpuTimeout = 4)
    @GradeFeedback(message = "Sorry, something is wrong cannot find the correct one tree", on = FAIL)
    @GradeFeedback(message = "Your one-tree algo is too slow", on = TIMEOUT)
    @ParameterizedTest(name = "[{index}] n5 - seed {0}")
    @MethodSource("getSeeds")
    public void testBruteForceVsSimpleOneTree(int seed) {
        TSPInstance instance = new TSPInstance(5,seed,100);


        // remove some edges to do as if we had already decided the partial tour 0->1->2
        boolean [][] excluded = new boolean[instance.n][instance.n];
        LinkedList<Integer> partialTour = new LinkedList<>();
        partialTour.add(0);
        partialTour.add(1);
        partialTour.add(2);


        for (int i = 0; i < instance.n; i++) {
            excluded[i][i] = true;
        }
        for (int i = 1; i < partialTour.size()-1; i++) {
            int a = partialTour.get(i-1);
            int b = partialTour.get(i);
            int c = partialTour.get(i+1);
            // we have a->b->c so b all the edges {b,k} with k != a and k!= c are not allowed
            for (int k = 0; k < instance.n; k++) {
                if (k != a && k != c) {
                    excluded[b][k] = true;
                    excluded[k][b] = true;
                }
            }
        }
        for (int i = 0; i < partialTour.size()-1; i++) {
            int a = partialTour.get(i);
            int b = partialTour.get(i+1);
            // we have a -> b
            // no reverse edge
            excluded[b][a] = true;
            // no one except b can be a successor of a
            for (int k = 0; k < instance.n; k++) {
                if (k != b) {
                    excluded[a][k] = true;
                }
            }
            // no one except a can be a predecessor of b
            for (int k = 0; k < instance.n; k++) {
                if (k != a) {
                    excluded[k][b] = true;
                }
            }
        }
        for (int i = 0; i < partialTour.size()-1; i++) {
            int n1 = partialTour.get(i);
            for (int j = i + 1; j < Math.min(partialTour.size(), instance.n-1); j++) {
                int n2 = partialTour.get(j);
                excluded[n2][n1] = true;
            }
        }

        TSPLowerBoundResult r1 = bruteForceOneTree(instance.distanceMatrix, excluded);
        TSPLowerBoundResult r2 = new OneTreeLowerBound().compute(instance.distanceMatrix, excluded);

        assertEquals(r1.lb(),r2.lb(),0.0001);

        assertTrue(TSPUtil.isOneTree(instance.n,r2.edges()));
        assertTrue(TSPUtil.isOneTree(instance.n,r1.edges()));
        assertTrue(edgePresent(1,2,r2.edges()));
        assertFalse(edgePresent(2,0,r2.edges()));
        assertFalse(edgePresent(0,2,r1.edges()));
    }

    public static List<Integer> getSeeds() {
        return IntStream.range(20, 40).boxed().collect(Collectors.toList());
    }

    private static TSPLowerBoundResult bruteForceOneTree(double [][] dist, boolean[][] excluded) {
        Set<Edge> edges = new HashSet<>();
        for (int i = 0; i < dist.length; i++) {
            for (int j = i+1; j < dist.length; j++) {
                edges.add(new Edge(i,j,dist[i][j]));
                edges.add(new Edge(j,i,dist[i][j]));
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
