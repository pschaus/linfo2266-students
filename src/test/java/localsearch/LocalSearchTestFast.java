package localsearch;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

@RunWith(Enclosed.class)
public class LocalSearchTestFast {

    public static class TestNotParameterized {
        @Test
        @Grade(value = 1, cpuTimeout = 2000)
        @GradeFeedback(message = "Your solution is not good enough", onFail = true)
        @GradeFeedback(message = "Please terminate the search within the given time limit", onTimeout = true)
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
    }
}
