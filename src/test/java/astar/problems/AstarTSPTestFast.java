package astar.problems;

import astar.solver.*;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.tsp.TSPInstance;

import java.util.List;

import static astar.problems.AstarTSPTest.testSolvingOptimality;
import static astar.problems.AstarTSPTest.getTSPInstances;
import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;

public class AstarTSPTestFast {

    public static List<Arguments> getTSPInstances8() {
        return getTSPInstances(8);
    }

    public static List<Arguments> getTSPInstances10() {
        return getTSPInstances(10);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your TSP model is wrong. Is your h value consistent ? Is your state definition correct", on = FAIL)
    @GradeFeedback(message = "The model is two slow. Is your h value to heavy to compute ? ", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances8")
    public void testOptimality8Astar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        Astar<TSPState> solver = new Astar<>(model);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your TSP model is wrong. Is your h value consistent ? Is your state definition correct", on = FAIL)
    @GradeFeedback(message = "The model is two slow. Is your h value to heavy to compute ? ", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances10")
    public void testOptimality10Astar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        Astar<TSPState> solver = new Astar<>(model);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your Weighted A* solver might be wrong. Weighted A* with hWeight set to 1 should return the optimal solution.", on = FAIL)
    @GradeFeedback(message = "Your Weighted A* solver is too slow.", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances8")
    public void testOptimality8WAstar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        WeightedAstar<TSPState> solver = new WeightedAstar<>(model, 1.0);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your Weighted A* solver might be wrong. Weighted A* with hWeight set to 1 should return the optimal solution.", on = FAIL)
    @GradeFeedback(message = "Your Weighted A* solver is too slow.", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances10")
    public void testOptimality10WAstar(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        WeightedAstar<TSPState> solver = new WeightedAstar<>(model, 1.0);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your Anytime Weighted A* solver might be wrong. Is your h function correct ? Are some nodes pruned by accident ? ", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. It should be able to return the optimal solution for each of these instances", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances8")
    public void testOptimality8AWA(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        AnytimeWeightedAstar<TSPState> solver = new AnytimeWeightedAstar<>(model, 2.0, 2);
        testSolvingOptimality(instance, solver);
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your Anytime Weighted A* solver might be wrong. Is your h function correct ? Are some nodes pruned by accident ? ", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. It should be able to return the optimal solution for each of these instances", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances10")
    public void testOptimality10AWA(TSPInstance instance) {
        TSPInstance clone = new TSPInstance(instance.distanceMatrix);
        TSP model = new TSP(clone);
        AnytimeWeightedAstar<TSPState> solver = new AnytimeWeightedAstar<>(model, 2.0, 2);
        testSolvingOptimality(instance, solver);
    }




}
