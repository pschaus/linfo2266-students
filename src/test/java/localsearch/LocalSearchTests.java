package localsearch;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import util.Pair;
import util.tsp.TSPInstance;

import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade(value = 7)
public class LocalSearchTests {

    public static List<Pair<TSPInstance, Double>> getTSPInstances(int size, double gap) {
        List<Pair<TSPInstance, Double>> instances = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            TSPInstance instance = new TSPInstance("data/TSP/instance_" + size + "_" + i + ".xml");

            instances.add(new Pair<>(instance, instance.objective * gap));
        }
        return instances;
    }

    public void testLocalSearch(LocalSearch ls, Double score) {
        Candidate c = ls.run();
        assertEquals(ls.it, 200, "Local Search should be limited to 200 iterations");
        // System.out.println("cost : " + c.getCost() + " threshold : " + score);
        assertTrue(c.getCost() <= score, "Local search should return a better solution");
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testBasic() {
        List<Pair<TSPInstance, Double>> instances = getTSPInstances(20, 1.10);
        for (Pair<TSPInstance, Double> i : instances) {
            LocalSearch localSearch = new LocalSearch(new FirstSelection(), new DefaultInitialization(i.getFirst()));
            testLocalSearch(localSearch, i.getSecond());
        }
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testBestSelection() {
        List<Pair<TSPInstance, Double>> instances = getTSPInstances(20, 1.10);
        for (Pair<TSPInstance, Double> i : instances) {
            LocalSearch localSearch = new LocalSearch(new BestSelection(), new DefaultInitialization(i.getFirst()));
            testLocalSearch(localSearch, i.getSecond());
        }
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testKOpt() {
        List<Pair<TSPInstance, Double>> instances = getTSPInstances(20, 1.05);
        for (Pair<TSPInstance, Double> i : instances) {
            LocalSearch localSearch = new LocalSearch(new KOptSelection(10), new DefaultInitialization(i.getFirst()));
            testLocalSearch(localSearch, i.getSecond());
        }
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testKTabu() {
        List<Pair<TSPInstance, Double>> instances = getTSPInstances(20, 1.07);
        for (Pair<TSPInstance, Double> i : instances) {
            LocalSearch localSearch = new LocalSearch(new BestWithTabuSelection(3, i.getFirst()), new DefaultInitialization(i.getFirst()));
            testLocalSearch(localSearch, i.getSecond());
        }
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testBeamSearch() {
        List<Pair<TSPInstance, Double>> instances = getTSPInstances(20, 1.08);
        for (Pair<TSPInstance, Double> i : instances) {
            LocalSearch localSearch = new LocalSearch(new FirstSelection(), new BeamSearchInitialization(i.getFirst(), 10));
            testLocalSearch(localSearch, i.getSecond());
        }
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testPilot() {
        List<Pair<TSPInstance, Double>> instances = getTSPInstances(20, 1.08);
        for (Pair<TSPInstance, Double> i : instances) {
            LocalSearch localSearch = new LocalSearch(new FirstSelection(), new PilotInitialization(i.getFirst()));
            testLocalSearch(localSearch, i.getSecond());
        }
    }


}
