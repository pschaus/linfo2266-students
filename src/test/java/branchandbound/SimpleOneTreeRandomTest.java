package branchandbound;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.UF;
import util.tsp.TSPInstance;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class SimpleOneTreeRandomTest {

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestRandom {
        final TSPInstance instance;

        public TestRandom(Integer seed) {
            this.instance = new TSPInstance(5,seed,100);
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            List<Integer> seed = new LinkedList<>();
            for (int i = 0; i < 20; i++) {
                seed.add(i);
            }
            return seed;
        }

        @Test
        @Grade(value = 5, cpuTimeout = 4000)
        @GradeFeedback(message = "Sorry, something is wrong cannot find the correct one tree", onFail=true)
        @GradeFeedback(message = "Your one-tree algo is too slow", onTimeout=true)
        public void testBruteForceVsSimpleOneTree() throws Exception {
            boolean [][] excluded = new boolean[instance.n][instance.n];
            excluded[2][3] = true;
            excluded[3][2] = true;
            excluded[4][1] = true;
            excluded[1][4] = true;

            OneTreeResult r1 = bruteForceOneTree(instance.distanceMatrix, excluded);
            OneTreeResult r2 = new SimpleOneTree().compute(instance.distanceMatrix, excluded);

            assertEquals(r1.lb(),r2.lb(),0.0001);
            assertTrue(isOneTree(instance.n,r2.edges()));
            assertTrue(isOneTree(instance.n,r1.edges()));
            assertFalse(edgePresent(2,3,r2.edges()));
            assertFalse(edgePresent(2,3,r1.edges()));


        }
    }


    private static OneTreeResult bruteForceOneTree(double [][] dist, boolean[][] excluded) {
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
        for (Set<Edge> candidate: powerSet(edges)) {
            if (isOneTree(dist.length, candidate)  && candidate.stream().allMatch(e -> !excluded[e.v1()][e.v2()])) {
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
        return new OneTreeResult(bestOneTreeCost, bestOneTree.stream().collect(Collectors.toList()));
    }




    public static boolean isOneTree(int n, Iterable<Edge> edges) {
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

    public static boolean edgePresent(int a, int b, List<Edge> edges) {
        for (Edge e: edges) {
            if (e.v1() == a && e.v2() == b) return true;
            if (e.v2() == a && e.v1() == b) return true;
        }
        return false;
    }


}
