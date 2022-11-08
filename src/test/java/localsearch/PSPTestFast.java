package localsearch;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

@RunWith(Enclosed.class)
public class PSPTestFast {

    static Random random = new Random(0);

    public static class TestNotParameterized {
        @Test
        @Grade(value = 1, cpuTimeout = 1000)
        @GradeFeedback(message = "Your feasibility check is not correct", onFail = true)
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

                            assertEquals(
                                feasible ? ("Swap " + swap + " on candidate " + candidate + " is said not feasible while it is")
                                         : ("Swap " + swap + " on candidate " + candidate + " is said feasible while it is not"), 
                                feasible, 
                                problem.isFeasible(candidate, swap)
                            );

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

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized {

        final PSPInstance instance;

        public TestParameterized(String name, PSPInstance instance) {
            this.instance = instance;
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return Utils.readPSPInstances("data/PSP/medium");
        }

        @Test
        @Grade(value = 1, cpuTimeout = 2000)
        @GradeFeedback(message = "Your feasibility check is not correct", onFail=true)
        public void testSwapFeasibleVSFromScratch() throws Exception {
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

                            assertEquals(
                                feasible ? ("Swap " + swap + " on candidate " + candidate + " is said not feasible while it is")
                                         : ("Swap " + swap + " on candidate " + candidate + " is said feasible while it is not"), 
                                feasible, 
                                problem.isFeasible(candidate, swap)
                            );

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
}
