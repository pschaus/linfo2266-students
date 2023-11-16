package localsearch;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Grade
public class ChangeoverCostInvariantTestFast {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Your computation of changeover costs is not correct", on = FAIL)
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

        // create production variables, initialized to IDLE
        IntVar[] production = IntVar.makeIntVarArray(nPeriods, PSP.IDLE);

        // create invariant
        ChangeoverCostInvariant invariant = new ChangeoverCostInvariant(instance, production);

        try {
            assertEquals(0, invariant.getValue());

            production[0].setValue(2);

            assertEquals(0, invariant.getValue());

            production[1].setValue(0);

            assertEquals(10, invariant.getValue());

            production[2].setValue(1);

            assertEquals(15, invariant.getValue());

            production[3].setValue(3);

            assertEquals(35, invariant.getValue());

            production[4].setValue(4);

            assertEquals(40, invariant.getValue());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(value = 0, cpuTimeout = 1)
    @GradeFeedback(message = "Your computation of changeover costs is not correct", on = FAIL)
    public void smallTestToDebug() {
        // instance data
        int nTypes = 3, nPeriods = 7;
        int[] stockingCost = {15, 10, 20};
        int[][] changeoverCost = {
                {0, 10, 15},
                {10,  0, 5},
                {10, 20, 0}
        };
        int[][] demand = {
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0 ,0, 1, 0},
        };

        // create instance
        PSPInstance instance = new PSPInstance(nPeriods, nTypes, stockingCost, changeoverCost, demand);

        // demands array looks like this
        //  0                       1                       2                       3                       4                       5
        // [(type: 0, deadline: 1), (type: 1, deadline: 2), (type: 1, deadline: 4), (type: 2, deadline: 5), (type: 0, deadline: 6), (type: 1, deadline: 6)]

        // create production variables, initialized to IDLE
        IntVar[] production = IntVar.makeIntVarArray(nPeriods, PSP.IDLE);

        // create invariant
        ChangeoverCostInvariant invariant = new ChangeoverCostInvariant(instance, production);

        try {
            assertEquals(0, invariant.getValue());

            production[1].setValue(0);

            assertEquals(0, invariant.getValue());

            production[3].setValue(3);

            assertEquals(15, invariant.getValue());

            production[0].setValue(1);

            assertEquals(25, invariant.getValue());

            production[2].setValue(2);

            assertEquals(25, invariant.getValue());

            production[4].setValue(5);

            assertEquals(45, invariant.getValue());

            production[5].setValue(4);

            assertEquals(55, invariant.getValue());

            // set some production to idle and check that the invariant is still correctly computed

            production[5].setValue(-1);

            assertEquals(45, invariant.getValue());

            production[4].setValue(-1);

            assertEquals(25, invariant.getValue());

            production[2].setValue(-1);

            assertEquals(25, invariant.getValue());

            production[0].setValue(-1);

            assertEquals(15, invariant.getValue());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

}
