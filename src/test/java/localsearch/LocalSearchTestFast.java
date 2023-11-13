package localsearch;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;
import org.javagrader.GradeFeedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class LocalSearchTestFast {

    @Test
    @Grade(value = 1, cpuTimeout = 3, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @GradeFeedback(message = "Your solution is not good enough", on = FAIL)
    @GradeFeedback(message = "Please terminate the search within the given time limit", on = TIMEOUT)
    public void readableTestToDebug() {
        // instance data
        int nTypes = 3, nPeriods = 5;
        int[] stockingCost = {10, 20, 30};
        int[][] changeoverCost = {
            {0, 10, 15},
            {10,  0, 5},
            {10, 20, 0}
        };
        int[][] demand = {
            {0, 0, 0, 0, 1},
            {0, 0, 0, 1, 1},
            {0, 0, 0, 1, 1},
        };

        // create instance
        PSPInstance instance = new PSPInstance(nPeriods, nTypes, stockingCost, changeoverCost, demand);

        // demands array looks like this
        //  0                       1                       2                       3                       4
        // [(type: 1, deadline: 3), (type: 2, deadline: 3), (type: 0, deadline: 4), (type: 1, deadline: 4), (type: 2, deadline: 4)]

        try {
            PSP problem = new PSP(instance);
            LocalSearch localSearch = new LocalSearch(problem);

            Candidate candidate = localSearch.solve(1);

            Utils.assertFeasible(instance, candidate);

            assertEquals(135, candidate.getValue());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 6, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @GradeFeedback(message = "Your solution is not good enough", on = FAIL)
    @GradeFeedback(message = "Please terminate the search within the given time limit", on = TIMEOUT)
    public void testResetSearchAndMaybeUpdate() {
        // create instance
        PSPInstance instance = new PSPInstance("data/PSP/medium/psp_100_5_90_10_0");

        try {
            PSP problem = new PSP(instance);
            LocalSearch localSearch = new LocalSearch(problem);

            for (int i = 0; i < 3 ; i++) {
                Candidate best = localSearch.solve(1);
                Utils.assertFeasible(instance, best);

                localSearch.resetSearch();
                assertFalse(localSearch.maybeSaveCurrentCandidate(), "The current solution should not be set as " +
                        "the best when its value is worse than the best value registered");
                Candidate current = localSearch.currentCandidate;
                Utils.assertFeasible(instance, current);
                assertNotEquals(current.getValue(), best.getValue());
                assertTrue(current.getValue() > best.getValue());
            }

        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 3, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @GradeFeedback(message = "Your solution is not good enough", on = FAIL)
    @GradeFeedback(message = "Please terminate the search within the given time limit", on = TIMEOUT)
    public void testGetNBestSwaps1() {
        // instance data
        int nTypes = 3, nPeriods = 5;
        int[] stockingCost = {10, 20, 30};
        int[][] changeoverCost = {
                {0, 10, 15},
                {10,  0, 5},
                {10, 20, 0}
        };
        int[][] demand = {
                {0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1},
                {0, 0, 0, 1, 1},
        };

        // create instance
        PSPInstance instance = new PSPInstance(nPeriods, nTypes, stockingCost, changeoverCost, demand);

        // demands array looks like this
        //  0                       1                       2                       3                       4
        // [(type: 1, deadline: 3), (type: 2, deadline: 3), (type: 0, deadline: 4), (type: 1, deadline: 4), (type: 2, deadline: 4)]

        try {
            PSP problem = new PSP(instance);
            LocalSearch localSearch = new LocalSearch(problem);

            Candidate candidate = localSearch.solve(1); // ensures that the data structures are correctly setup
            Utils.assertFeasible(instance, candidate);

            localSearch.resetSearch(); // reset to provide some relevant neighborhoods
            candidate = localSearch.currentCandidate;
            Utils.assertFeasible(instance, candidate);
            for (int i = 1; i < 7 ; i++) {
                List<Swap> oldBestSwaps = localSearch.getNBestSwaps(i);
                for (Swap swap : oldBestSwaps) {
                    Candidate copy = candidate.copy();
                    copy.apply(swap);
                    assertTrue(Utils.isFeasible(instance, copy), "Each swap being returned must be feasible");
                }
            }

        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 3, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @GradeFeedback(message = "Your solution is not good enough", on = FAIL)
    @GradeFeedback(message = "Please terminate the search within the given time limit", on = TIMEOUT)
    public void testGetNBestSwaps2() {
        // instance data
        int nTypes = 3, nPeriods = 5;
        int[] stockingCost = {10, 20, 30};
        int[][] changeoverCost = {
                {0, 10, 15},
                {10,  0, 5},
                {10, 20, 0}
        };
        int[][] demand = {
                {0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1},
                {0, 0, 0, 1, 1},
        };

        // create instance
        PSPInstance instance = new PSPInstance(nPeriods, nTypes, stockingCost, changeoverCost, demand);

        // demands array looks like this
        //  0                       1                       2                       3                       4
        // [(type: 1, deadline: 3), (type: 2, deadline: 3), (type: 0, deadline: 4), (type: 1, deadline: 4), (type: 2, deadline: 4)]

        try {
            PSP problem = new PSP(instance);
            LocalSearch localSearch = new LocalSearch(problem);

            Candidate candidate = localSearch.solve(1); // ensures that the data structures are correctly setup
            Utils.assertFeasible(instance, candidate);

            localSearch.resetSearch(); // reset to provide some relevant neighborhoods
            candidate = localSearch.currentCandidate;
            Utils.assertFeasible(instance, candidate);

            for (int i = 1; i < 7 ; i++) {
                List<Swap> oldBestSwaps = localSearch.getNBestSwaps(i);
                assertEquals(i, oldBestSwaps.size());
                for (int k = 0 ; k < i ; k++)
                    for (int l = k+1 ; l < i ; l++)
                        assertNotEquals(oldBestSwaps.get(k), oldBestSwaps.get(l), "Each type of swap must appear at most once");
                // ensure that the swaps are feasible
                for (int j = i+1; j < 8; j++) {
                    List<Swap> currentBestSwaps = localSearch.getNBestSwaps(j);
                    assertEquals(j, currentBestSwaps.size());
                    /*

                     */
                    for (int k = 0 ; k < j ; k++)
                        for (int l = k+1 ; l < j ; l++)
                            assertNotEquals(currentBestSwaps.get(k), currentBestSwaps.get(l), "Each type of swap must appear at most once");
                    for (Swap swap: currentBestSwaps)
                        assertEquals(swap.getDelta(), candidate.getDelta(swap), "You need to assign some value to the deltas of the swaps");
                    for (Swap swap : oldBestSwaps) {
                        assertTrue(currentBestSwaps.contains(swap), "The x best swaps must be contained within the x+1 best swaps");
                        assertTrue(currentBestSwaps.stream().map(Swap::getDelta).collect(Collectors.toList()).contains(swap.getDelta()),
                                "The x best swaps must be contained within the x+1 best swaps");
                    }
                    int nSwapsSeen = 0;
                    for (Swap swap : currentBestSwaps) {
                        if (oldBestSwaps.contains(swap))
                            nSwapsSeen += 1;
                    }
                    assertEquals(oldBestSwaps.size(), nSwapsSeen, "All x best swaps must appear within the x+1 best swaps");
                    assertTrue(currentBestSwaps.stream().anyMatch(swap -> swap.getDelta() != 0), "You need to assign some value to the deltas of the swaps");
                    assertTrue(currentBestSwaps.stream().allMatch(swap -> swap.getDelta() >= 0), "You need to assign some value to the deltas of the swaps");
                    if (currentBestSwaps.size() > oldBestSwaps.size() + 3) {
                        int currentWorse = currentBestSwaps.stream().mapToInt(Swap::getDelta).max().getAsInt();
                        oldBestSwaps.forEach(swap -> assertTrue(swap.getDelta() < currentWorse, "You need to assign some value to the deltas of the swaps"));
                    }
                    for (Swap swap: currentBestSwaps) {
                        Candidate copy = candidate.copy();
                        copy.apply(swap);
                        assertTrue(Utils.isFeasible(instance, copy), "Each swap being returned must be feasible");
                    }
                }
                for (Swap swap: oldBestSwaps) {
                    Candidate copy = candidate.copy();
                    copy.apply(swap);
                    assertTrue(Utils.isFeasible(instance, copy), "Each swap being returned must be feasible");
                }
            }


        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @GradeFeedback(message = "Your solution is not good enough", on = FAIL)
    @GradeFeedback(message = "Please terminate the search within the given time limit", on = TIMEOUT)
    public void testSelectSwap() {
        // create instance
        PSPInstance instance = new PSPInstance("data/PSP/medium/psp_100_5_90_10_0");

        try {
            PSP problem = new PSP(instance);
            LocalSearch localSearch = new LocalSearch(problem);

            Candidate best = localSearch.solve(1);
            Utils.assertFeasible(instance, best);

            localSearch.resetSearch();
            Candidate current = localSearch.currentCandidate;
            List<Swap> swaps = localSearch.getNBestSwaps(10);
            assertEquals(10, swaps.size());
            Map<Swap, Integer> cnt = new HashMap<>(); // number of time a swap has been chosen
            for (Swap swap: swaps)
                cnt.put(swap, 0);
            int iter = 1_000_000;
            for (int j = 0 ; j < iter ; j++) {
                Swap chosen = localSearch.selectSwap(swaps);
                cnt.put(chosen, cnt.get(chosen) + 1);
            }
            for (Swap swap: swaps) {
                int nChosen = cnt.get(swap);
                assertTrue(nChosen < iter * 0.8, "A swap should not always be chosen within the list");
                assertTrue(nChosen > iter * 0.001, "Every swap within the list must have a chance to be chosen");
            }
            Utils.assertFeasible(instance, best);
            assertNotEquals(current.getValue(), best.getValue());
            assertTrue(current.getValue() > best.getValue());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }


}
