package astar.problems;

import astar.solver.*;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.knapsack.KnapsackInstance;

import java.util.List;

import static astar.problems.AstarKnapsackTest.*;
import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;

@Grade
public class AstarKnapsackTestFast {

    public static List<Arguments> getKnapsackInstances100() {
        return getKnapsackInstances(100);
    }


    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "Something in your Weighted A* solver might be wrong. Weighted A* with hWeight set to 1 should return the optimal solution.", on = FAIL)
    @GradeFeedback(message = "Your Weighted A* solver is too slow.", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getKnapsackInstances100")
    public void testOptimality100WA(KnapsackInstance instance) {
        KnapsackInstance clone = new KnapsackInstance(instance.capacity, instance.value, instance.weight);
        Knapsack model = new Knapsack(clone);
        WeightedAstar<KnapsackState> solver = new WeightedAstar<>(model, 1.0);
        testSolvingOptimality(instance, solver);
    }


    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "Something in your Anytime Weighted A* solver might be wrong. Is your h function correct ? Are some nodes pruned by accident ? ", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. It should be able to return the optimal solution for each of these instances", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getKnapsackInstances100")
    public void testOptimality100AWA(KnapsackInstance instance) {
        KnapsackInstance clone = new KnapsackInstance(instance.capacity, instance.value, instance.weight);
        Knapsack model = new Knapsack(clone);
        AnytimeWeightedAstar<KnapsackState> solver = new AnytimeWeightedAstar<>(model, 0.5, 2);
        testSolvingOptimality(instance, solver);
    }



}
