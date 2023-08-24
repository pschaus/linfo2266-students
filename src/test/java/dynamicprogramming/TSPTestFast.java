package dynamicprogramming;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.tsp.TSPInstance;

import java.util.List;

import static dynamicprogramming.TSPTest.assertSolvingOptimality;
import static dynamicprogramming.TSPTest.getTSPInstances;
import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;

@Grade
public class TSPTestFast {

    public static List<Arguments> getTSPInstances8() {
        return getTSPInstances(8);
    }

    public static List<Arguments> getTSPInstances10() {
        return getTSPInstances(10);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle minimization properly?", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances8")
    public void testOptimality8(TSPInstance instance) {
        assertSolvingOptimality(instance);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle minimization properly?", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances10")
    public void testOptimality10(TSPInstance instance) {
        assertSolvingOptimality(instance);
    }

}
