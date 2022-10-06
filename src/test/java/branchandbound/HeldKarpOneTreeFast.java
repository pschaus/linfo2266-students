package branchandbound;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import org.junit.Test;
import util.UF;
import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class HeldKarpOneTreeFast {


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", onFail = true)
    public void smallExample() {
        // see here for the graph https://pasteboard.co/iVjXKUlNE6ev.png
        double [][] distanceMatrix = new double[7][7];
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 5, 5));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(1, 6, 7));
        edges.add(new Edge(1, 5, 1));
        edges.add(new Edge(2, 5, 4));
        edges.add(new Edge(2, 4, 7));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(3, 4, 5));
        edges.add(new Edge(4, 5, 2));
        edges.add(new Edge(4, 6, 8));
        edges.add(new Edge(5, 6, 6));

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

        OneTreeResult res = new HeldKarpOneTree().compute(distanceMatrix, excluded);

        assertEquals(29, res.lb(), 0.1);

        checkOneTree(7,res.edges());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    @GradeFeedback(message = "More complex test", onFail = true)
    public void smallExampleOneEdgeExcluded() {
        // see here for the graph https://pasteboard.co/iVjXKUlNE6ev.png
        double [][] distanceMatrix = new double[7][7];
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 5, 5));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(1, 6, 7));
        edges.add(new Edge(1, 5, 1));
        edges.add(new Edge(2, 5, 4));
        edges.add(new Edge(2, 4, 7));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(3, 4, 5));
        edges.add(new Edge(4, 5, 2));
        edges.add(new Edge(4, 6, 8));
        edges.add(new Edge(5, 6, 6));

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
