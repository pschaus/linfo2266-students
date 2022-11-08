package localsearch;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;
import util.psp.PSPInstance.Demand;

import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class ChangeoverCostInvariantTest {

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized {

        static Random random = new Random(0);
        final PSPInstance instance;

        public TestParameterized(String name, PSPInstance instance) {
            this.instance = instance;
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return Utils.readPSPInstances("data/PSP/large");
        }

        @Test
        @Grade(value = 1, cpuTimeout = 2000)
        @GradeFeedback(message = "Your computation of changeover costs is not correct", onFail=true)
        @GradeFeedback(message = "Your computation of changeover costs is too slow, are you doing incremental updates?", onTimeout=true)
        public void testInvariantVSFromScratch() throws Exception {
            try {
                // create production variables, initialized to IDLE
                IntVar[] production = IntVar.makeIntVarArray(instance.nPeriods, PSP.IDLE);

                // create invariant
                ChangeoverCostInvariant invariant = new ChangeoverCostInvariant(instance, production);

                for (int i = 0; i < 10000; i++) {
                    int period = random.nextInt(instance.nPeriods);
                    int demand = random.nextInt(instance.demands.length);

                    production[period].setValue(demand);

                    assertEquals(changeoverCost(instance, production), invariant.getValue());
                }
            } catch (NotImplementedException e) {
                NotImplementedExceptionAssume.fail(e);
            }
        }
    }

    private static int changeoverCost(PSPInstance instance, IntVar[] production) {
        int cost = 0;
        int lastType = PSP.IDLE;

        for (int p = 0; p < instance.nPeriods; p++) {
            int demandId = production[p].getValue();
            if (demandId != PSP.IDLE) {
                Demand demand = instance.demands[demandId];
                if (lastType != PSP.IDLE) cost += instance.changeoverCost[lastType][demand.type];
                lastType = demand.type;
            }
        }

        return cost;
    }

}
