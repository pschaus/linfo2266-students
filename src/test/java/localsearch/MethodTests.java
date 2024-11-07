package localsearch;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD;

@Grade(value = 7)
public class MethodTests {

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testSwap() {
        TSPInstance tsp = new TSPInstance("data/TSP/instance_8_0.xml");

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < tsp.nCities(); i++) {
            list.add(i);
        }
        Candidate c = new Candidate(tsp, list);
        c.twoOpt(0, 2);
        assertEquals(Arrays.asList(0, 2, 1, 3, 4, 5, 6, 7), c.getTour());
        c.twoOpt(7, 2);
        assertEquals(Arrays.asList(0, 2, 1, 7, 6, 5, 4, 3), c.getTour(), "index1 can be greater than index2");
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testBestNeighbor() {
        TSPInstance tsp = new TSPInstance("data/TSP/instance_8_0.xml");
        DefaultInitialization init = new DefaultInitialization(tsp);
        Candidate c1 = init.getInitialSolution();
        Candidate c2 = init.getInitialSolution();
        BestSelection selection = new BestSelection();
        FirstSelection firstSelection = new FirstSelection();
        Candidate neighbor1 = firstSelection.getNeighbor(c1);
        Candidate neighbor2 = selection.getNeighbor(c2);
        assertTrue(neighbor1.getCost() > neighbor2.getCost(), "BestSelection should yield a better solution");
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testKOpt() {
        TSPInstance tsp = new TSPInstance("data/TSP/instance_8_0.xml");
        DefaultInitialization init = new DefaultInitialization(tsp);
        Candidate c1 = init.getInitialSolution();
        Candidate c2 = init.getInitialSolution();
        KOptSelection selection1 = new KOptSelection(1);
        KOptSelection selection2 = new KOptSelection(5);
        Candidate neighbor1 = selection1.getNeighbor(c1);
        Candidate neighbor2 = selection2.getNeighbor(c2);
        assertTrue(neighbor1.getCost() > neighbor2.getCost(), "Higher k value should yield a better solution");

    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testTabu() {
        TSPInstance tsp = new TSPInstance("data/TSP/instance_8_0.xml");
        List<Integer> init = Arrays.asList(0, 1, 2, 4, 6, 5, 3, 7);
        Candidate c = new Candidate(tsp, init);
        double previousCost = c.getCost();
        BestWithTabuSelection tabu = new BestWithTabuSelection(10, tsp);
        Candidate nc = tabu.getNeighbor(c);
        assertTrue(nc.getCost() >= previousCost, "Tabu search should return the best neighbor found even if it's a worse candidate");
        assertTrue(tabu.isTabu(0, 1), "When applying a swap, the swap reverting to the previous solution should be prevented. Work with cities and not indexes.");
        assertFalse(tabu.isTabu(0, 2), "Only the previous swap should be added to tabu");
        nc = tabu.getNeighbor(nc);
        assertNotEquals(nc.getTour(), init, "Tabu should prevent a solution to be obtained multiple times");
    }

    public void testInitialization(Initialization init) {
        TSPInstance tsp = init.tsp;
        Candidate c = init.getInitialSolution();
        assertEquals(tsp.nCities(), c.getTour().size(), "The Tour size should be equal to the number of cities in the problem");
        boolean hasAllCities = true;
        for (int i = 0; i < c.getTour().size(); i++) {
            if (!c.getTour().contains(i)) {
                hasAllCities = false;
                break;
            }
        }
        assertTrue(hasAllCities, "The Tour should contain all cities in the problem");
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testBeamSearch() {
        TSPInstance tsp = new TSPInstance("data/TSP/instance_18_0.xml");

        BeamSearchInitialization init1 = new BeamSearchInitialization(tsp, 1);
        testInitialization(init1);
        Candidate c1 = init1.getInitialSolution();

        BeamSearchInitialization init2 = new BeamSearchInitialization(tsp, 100);
        Candidate c2 = init2.getInitialSolution();


        assertTrue(c1.getCost() >= c2.getCost(), "A larger beam size should yield a better solution");


    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void testPilot() {
        TSPInstance tsp = new TSPInstance("data/TSP/instance_18_0.xml");

        BeamSearchInitialization init1 = new BeamSearchInitialization(tsp, 1);
        Candidate c1 = init1.getInitialSolution();

        BeamSearchInitialization init2 = new PilotInitialization(tsp);
        testInitialization(init2);
        Candidate c2 = init2.getInitialSolution();


        assertTrue(c1.getCost() >= c2.getCost(), "A pilot initialization size should yield a better solution than a beam search with k = 1");


    }


}
