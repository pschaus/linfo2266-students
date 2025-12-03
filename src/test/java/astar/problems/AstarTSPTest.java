package astar.problems;

import astar.solver.*;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class AstarTSPTest {

    public static List<Arguments> getTSPInstances18() {
        return getTSPInstances(18);
    }

    public static List<Arguments> getTSPInstances30() {
        return getTSPInstances(30);
    }

    public static List<Arguments> getTSPInstances20() {
        return getTSPInstances(20);
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

    public static void testSolvingOptimality(TSPInstance instance, Solver solver) {
        List<Solution> solutions = new ArrayList<>();

        solver.solve((solution, stats) -> {
            solutions.add(solution);
        });

        // Check is the last found solution is optimal
        assertEquals(instance.objective, solutions.get(solutions.size() - 1).getValue(), 1e-3);

        // Check for each found solution if it is valid and if its cost is correct
        for (Solution solution: solutions) {
            double checkValue = 0;
            int position = 0;
            Set<Integer> decisions = new HashSet<>();
            for (int decision : solution.getDecisions()) {
                assertFalse(decisions.contains(decision));
                decisions.add(decision);
                checkValue += instance.distanceMatrix[position][decision];
                position = decision;
            }
            checkValue += instance.distanceMatrix[position][0]; // back to initial position

            assertEquals(solution.getValue(), checkValue, 1e-3);
        }
    }

    public static void testSolvingValidity(TSPInstance instance, Solver solver) {
        List<Solution> solutions = new ArrayList<>();

        solver.solve((solution, stats) -> {
            solutions.add(solution);
        });

        assertFalse(solutions.isEmpty());

        // Check for each found solution if it is valid and if its cost is correct
        for (Solution solution: solutions) {
            double checkValue = 0;
            int position = 0;
            Set<Integer> decisions = new HashSet<>();
            for (int decision : solution.getDecisions()) {
                assertFalse(decisions.contains(decision));
                decisions.add(decision);
                checkValue += instance.distanceMatrix[position][decision];
                position = decision;
            }
            checkValue += instance.distanceMatrix[position][0]; // back to initial position

            assertEquals(solution.getValue(), checkValue, 1e-3);
        }
    }

    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your TSP model is wrong. Is your h value consistent ? Is your state definition correct", on = FAIL)
    @GradeFeedback(message = "The model is two slow. Is your h value to heavy to compute ? ", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances18")
    public void testOptimality18Astar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        Astar<TSPState> solver = new Astar<>(model);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your Weighted A* solver might be wrong. Weighted A* with hWeight set to 1 should return the optimal solution.", on = FAIL)
    @GradeFeedback(message = "Your Weighted A* solver is too slow.", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances18")
    public void testOptimality18WAstar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        WeightedAstar<TSPState> solver = new WeightedAstar<>(model, 1.0);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your Weighted A* solver might be wrong. Is your state definition correct ?", on = FAIL)
    @GradeFeedback(message = "Your weighted A* solver is too slow. Is the hWeight used ? ", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances30")
    public void testValidity30WAstar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        WeightedAstar<TSPState> solver = new WeightedAstar<>(model, 2.5);
        testSolvingValidity(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your Anytime Weighted A* solver might be wrong. Is your state definition correct ?", on = FAIL)
    @GradeFeedback(message = "Your solver is not able to find a first solution quickly enough. Is it able to stop when the timelimit is reached ? ", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances30")
    public void testValidity30AWA(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        AnytimeWeightedAstar<TSPState> solver = new AnytimeWeightedAstar<>(model, 2.5, 2);
        testSolvingValidity(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 5)
    @GradeFeedback(message = "Something in your Anytime Weighted A* solver might be wrong. Is your h function correct ? Are some nodes pruned by accident ? ", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. It should be able to return the optimal solution for each of these instances", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances18")
    public void testOptimality18AWA(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        AnytimeWeightedAstar<TSPState> solver = new AnytimeWeightedAstar<>(model, 2.5, 5);
        testSolvingOptimality(instance, solver);
    }

}
