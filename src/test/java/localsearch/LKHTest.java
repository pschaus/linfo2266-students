package localsearch;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import util.Pair;
import util.tsp.TSPInstance;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD;
@Grade(value = 2)
public class LKHTest {

    @Test
    public void testTourToSucc() {
        TSPInstance instance = new TSPInstance("data/TSP/instance_8_0.xml");
        LKH lkh = new LKH(instance);

        Candidate c = new Candidate(instance, new int[]{0, 2, 1, 3});
        int[] succ = lkh.tourToSucc(c);

        int[] expected = {2, 3, 1, 0};
        assertArrayEquals(expected, succ,
                "tourToSucc should map each city to its next city (circularly). Check the modulo indexing.");
    }

    @Test
    public void testSuccToTour() {
        TSPInstance instance = new TSPInstance("data/TSP/instance_8_0.xml");
        LKH lkh = new LKH(instance);

        int[] succ = {1, 2, 3, 0}; // 0→1→2→3→0
        int[] tour = lkh.succToTour(succ);
        int[] expected = {0, 1, 2, 3};

        assertArrayEquals(expected, tour,
                "succToTour should rebuild the tour in order starting from city 0.");
    }

    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void simpleTest() {
        TSPInstance instance = new TSPInstance("data/TSP/instance_8_0.xml");
        LKH lkh = new LKH(instance);
        RandomInitialization init = new RandomInitialization(instance);
        Candidate initial = init.getInitialSolution();
        Candidate improved = lkh.applyLKH(initial);
        assertValidTour(improved.getTour());
        assertTrue(improved.getCost() < initial.getCost(),"LKH should return a better solution than the initial solution");
    }


    @Grade(value = 1, cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    @Test
    public void finalTest() {
        TSPInstance instance = new TSPInstance("data/TSP/instance_8_0.xml");
        LKH lkh = new LKH(instance);
        DefaultInitialization init = new DefaultInitialization(instance);
        Candidate initial = init.getInitialSolution();
        Candidate improved = lkh.applyLKH(initial);
        assertValidTour(improved.getTour());
        assertTrue(improved.getCost() <= 250, "LKH should return a solution with cost at most 250 for instance_8_0.xml");
    }

    private void assertValidTour(int[] tour) {
        boolean[] seen = new boolean[tour.length];
        for (int city : tour) {
            assertTrue(city >= 0 && city < tour.length,
                    "Tour must only contain city indices between 0 and " + (tour.length - 1));
            assertFalse(seen[city], "Tour must not repeat cities; duplicate: " + city);
            seen[city] = true;
        }
        for (int i = 0; i < seen.length; i++) {
            assertTrue(seen[i], "Tour missing city: " + i);
        }
    }
}
