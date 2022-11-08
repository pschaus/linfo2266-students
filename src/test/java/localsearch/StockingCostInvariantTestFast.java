package localsearch;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class StockingCostInvariantTestFast {

    public static class TestNotParameterized {
        @Test
        @Grade(value = 1, cpuTimeout = 1000)
        @GradeFeedback(message = "Your computation of stocking costs is not correct", onFail = true)
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
            StockingCostInvariant invariant = new StockingCostInvariant(instance, production);
            
            try {
                assertEquals(0, invariant.getValue());

                production[0].setValue(2);

                assertEquals(40, invariant.getValue());

                production[1].setValue(0);

                assertEquals(80, invariant.getValue());

                production[2].setValue(1);

                assertEquals(110, invariant.getValue());

                production[3].setValue(3);

                assertEquals(130, invariant.getValue());

                production[4].setValue(4);

                assertEquals(130, invariant.getValue());
            } catch (NotImplementedException e) {
                NotImplementedExceptionAssume.fail(e);
            }
        }
    }

}
