package dynamicprogramming;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.Solution;
import util.knapsack.KnapsackInstance;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import static dynamicprogramming.KnapsackTest.getKnapsackInstances;
import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Grade
public class KnapsackTestFast {

    public static List<Arguments> getKnapsackInstances100() {
        return getKnapsackInstances(100);
    }

    public static void testSolvingOptimality(KnapsackInstance instance) {
        KnapsackInstance clone = new KnapsackInstance(instance.capacity, instance.value, instance.weight);

        Knapsack model = new Knapsack(clone);
        DynamicProgramming<KnapsackState> solver = new DynamicProgramming<>(model);
        Solution solution = solver.getSolution();

        assertEquals(instance.objective, (int) solution.getValue());

        int checkValue = 0, checkWeight = 0, item = 0;
        for (int decision : solution.getDecisions()) {
            if (decision == 1) {
                checkValue += instance.value[item];
                checkWeight += instance.weight[item];
            }

            item++;
        }

        assertEquals(instance.objective, checkValue);
        assertTrue(checkWeight <= instance.capacity);
    }

    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle maximization properly?", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getKnapsackInstances100")
    public void testOptimality100(KnapsackInstance instance) {
        testSolvingOptimality(instance);
    }
    
}
