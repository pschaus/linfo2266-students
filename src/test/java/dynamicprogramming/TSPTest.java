package dynamicprogramming;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.Solution;
import util.tsp.TSPInstance;

import java.util.*;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class TSPTest {

    public static List<Arguments> getTSPInstances18() {
        return getTSPInstances(18);
    }

    public static List<Arguments> getTSPInstances(int size) {
        LinkedList<Arguments> coll = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            String name = "data/TSP/instance_"+size+"_"+i+".xml";
            String feature = "n" + size + "_" + i;
            coll.add(arguments(named(feature, new TSPInstance(name))));
        }
        return coll;
    }

    public static void assertSolvingOptimality(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);

        TSP model = new TSP(clone);
        DynamicProgramming<TSPState> solver = new DynamicProgramming<>(model);
        Solution solution = solver.getSolution();
        
        assertEquals(instance.objective, solution.getValue(), 1e-3);

        double checkValue = 0;
        int position = 0;
        for (int decision : solution.getDecisions()) {
            checkValue += instance.distanceMatrix[position][decision];
            position = decision;
        }
        checkValue += instance.distanceMatrix[position][0]; // back to initial position

        assertEquals(instance.objective, checkValue, 1e-3);
    }

    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle minimization properly?", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances18")
    public void testOptimality18(TSPInstance instance) {
        assertSolvingOptimality(instance);
    }

}
