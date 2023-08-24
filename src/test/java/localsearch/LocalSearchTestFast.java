package localsearch;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;
import org.javagrader.GradeFeedback;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Grade
public class LocalSearchTestFast {

    @Test
    @Grade(value = 1, cpuTimeout = 2)
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
}
