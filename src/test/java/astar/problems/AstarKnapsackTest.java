package astar.problems;

import astar.solver.*;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.Solution;
import util.knapsack.KnapsackInstance;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class AstarKnapsackTest {


    public static List<Arguments> getKnapsackInstances1000() {
        return getKnapsackInstances(1000);
    }

    public static List<Arguments> getKnapsackInstances(int size) {
        LinkedList<Arguments> coll = new LinkedList<>();

        File instanceDir = new File("data/Knapsack");
        File[] instances = instanceDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("instance_n" + size + "_");
            }
        });

        for (File instance : instances) {
            coll.add(arguments(named(instance.getName().replace("instance_", ""), new KnapsackInstance(instance.getAbsolutePath()))));
        }
        return coll;
    }


    public static void testSolvingOptimality(KnapsackInstance instance, Solver solver) {
        List<Solution> solutions = new ArrayList<>();

        solver.solve((solution, stats) -> {
            solutions.add(solution);
        });

        // Check is the last found solution is optimal
        assertEquals(instance.objective, -solutions.get(solutions.size() - 1).getValue(), 1e-3);

        // Check for each found solution if it is valid and if its cost is correct
        for (Solution solution: solutions) {
            int checkValue = 0, checkWeight = 0, item = 0;
            for (int decision : solution.getDecisions()) {
                if (decision == 1) {
                    checkValue += instance.value[item];
                    checkWeight += instance.weight[item];
                }

                item++;
            }
            assertTrue(checkWeight <= instance.capacity);
            assertEquals(solution.getValue(), -checkValue, 1e-3);
        }
    }

    public static void testSolvingValidity(KnapsackInstance instance, Solver solver) {
        List<Solution> solutions = new ArrayList<>();

        solver.solve((solution, stats) -> {
            solutions.add(solution);
        });

        assertFalse(solutions.isEmpty());

        // Check for each found solution if it is valid and if its cost is correct
        for (Solution solution: solutions) {
            int checkValue = 0, checkWeight = 0, item = 0;
            for (int decision : solution.getDecisions()) {
                if (decision == 1) {
                    checkValue += instance.value[item];
                    checkWeight += instance.weight[item];
                }

                item++;
            }
            assertTrue(checkWeight <= instance.capacity);
            assertEquals(solution.getValue(), -checkValue, 1e-3);
        }
    }



    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your Anytime Weighted A* solver might be wrong. Is your state definition correct ?", on = FAIL)
    @GradeFeedback(message = "Your solver is not able to find a first solution quickly enough. Is it able to stop when the timelimit is reached ? ", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getKnapsackInstances1000")
    public void testValidity1000AWA(KnapsackInstance instance) {
        KnapsackInstance clone = new KnapsackInstance(instance.capacity, instance.value, instance.weight);
        Knapsack model = new Knapsack(clone);
        AnytimeWeightedAstar<KnapsackState> solver = new AnytimeWeightedAstar<>(model, 0.5, 2);
        testSolvingValidity(instance, solver);
    }
}
