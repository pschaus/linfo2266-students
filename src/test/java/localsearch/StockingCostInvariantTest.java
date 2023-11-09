package localsearch;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;
import util.psp.PSPInstance.Demand;

import java.util.List;
import java.util.Random;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Grade(value = 2)
public class StockingCostInvariantTest {

    public static List<Arguments> getLargeInstances() {
        return Utils.readPSPInstances("data/PSP/large");
    }

    Random random = new Random();

    @Grade(value = 1, cpuTimeout = 2)
    @GradeFeedback(message = "Your computation of stocking costs is not correct", on = FAIL)
    @GradeFeedback(message = "Your computation of stocking costs is too slow, are you doing incremental updates?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getLargeInstances")
    public void testInvariantVSFromScratch(PSPInstance instance) {
        try {
            // create production variables, initialized to IDLE
            IntVar[] production = IntVar.makeIntVarArray(instance.nPeriods, PSP.IDLE);

            // create invariant
            StockingCostInvariant invariant = new StockingCostInvariant(instance, production);

            for (int i = 0; i < 10000; i++) {
                int period = random.nextInt(instance.nPeriods);
                int demand = random.nextInt(instance.demands.length);

                production[period].setValue(demand);

                assertEquals(stockingCost(instance, production), invariant.getValue());
            }
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    private static int stockingCost(PSPInstance instance, IntVar[] production) {
        int cost = 0;

        for (int p = 0; p < instance.nPeriods; p++) {
            int demandId = production[p].getValue();
            if (demandId != PSP.IDLE) {
                Demand demand = instance.demands[demandId];
                cost += (demand.deadline - p) * instance.stockingCost[demand.type];
            }
        }

        return cost;
    }

}
