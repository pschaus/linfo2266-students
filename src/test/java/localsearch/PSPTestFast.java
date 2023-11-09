package localsearch;

import java.util.*;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Grade(value = 2)
public class PSPTestFast {

    static Random random = new Random(0);

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Your feasibility check is not correct", on = FAIL)
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
            Candidate candidate = problem.getInitialCandidate();

            Utils.assertFeasible(instance, candidate);

            ArrayList<Integer> periods = new ArrayList<>();
            for (int p = 0; p < instance.nPeriods; p++) {
                periods.add(p);
            }

            for (int i = 0; i < 10; i++) {
                Collections.shuffle(periods);
                Swap swap = new Swap(), lastFeasible = null;

                for (int k = 0; k < 5; k++) {
                    swap = swap.getExtendedSwapWith(periods.get(k));

                    if (swap.variableIds.length >= 2) {
                        Candidate copy = candidate.copy();
                        copy.apply(swap);
                        boolean feasible = Utils.isFeasible(instance, copy);

                        assertEquals(feasible, problem.isFeasible(candidate, swap),
                                feasible ? ("Swap " + swap + " on candidate " + candidate + " is said not feasible while it is")
                                        : ("Swap " + swap + " on candidate " + candidate + " is said feasible while it is not"));

                        if (feasible) {
                            lastFeasible = swap;
                        }
                    }
                }

                if (lastFeasible != null) {
                    candidate.apply(lastFeasible);
                }
            }
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    public static List<Arguments> getMediumInstances() {
        return Utils.readPSPInstances("data/PSP/medium");
    }

    @Grade(value = 1, cpuTimeout = 2)
    @GradeFeedback(message = "Your feasibility check is not correct", on = FAIL)
    @ParameterizedTest
    @MethodSource("getMediumInstances")
    public void testSwapFeasibleVSFromScratch(PSPInstance instance) {
        try {
            PSP problem = new PSP(instance);
            Candidate candidate = problem.getInitialCandidate();

            Utils.assertFeasible(instance, candidate);

            ArrayList<Integer> periods = new ArrayList<>();
            for (int p = 0; p < instance.nPeriods; p++) {
                periods.add(p);
            }

            for (int i = 0; i < 10; i++) {
                Collections.shuffle(periods);
                Swap swap = new Swap(), lastFeasible = null;

                for (int k = 0; k < instance.nPeriods / 2; k++) {
                    swap = swap.getExtendedSwapWith(periods.get(k));

                    if (swap.variableIds.length >= 2) {
                        Candidate copy = candidate.copy();
                        copy.apply(swap);
                        boolean feasible = Utils.isFeasible(instance, copy);

                        assertEquals(feasible, problem.isFeasible(candidate, swap),
                                feasible ? ("Swap " + swap + " on candidate " + candidate + " is said not feasible while it is")
                                        : ("Swap " + swap + " on candidate " + candidate + " is said feasible while it is not"));

                        if (feasible) {
                            lastFeasible = swap;
                        }
                    }
                }

                if (lastFeasible != null) {
                    candidate.apply(lastFeasible);
                }
            }
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }
}
