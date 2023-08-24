package mdd;

import mdd.exercise.MaximumDecarbonationProblem;
import mdd.exercise.MaximumDecarbonationRelaxation;
import mdd.exercise.MaximumDecarbonationState;
import mdd.exercise.MaximumDecarbonationStateRanking;
import mdd.framework.core.Frontier;
import mdd.framework.core.Relaxation;
import mdd.framework.core.Solver;
import mdd.framework.heuristics.StateRanking;
import mdd.framework.heuristics.VariableHeuristic;
import mdd.framework.heuristics.WidthHeuristic;
import mdd.framework.implem.frontier.SimpleFrontier;
import mdd.framework.implem.heuristics.DefaultVariableHeuristic;
import mdd.framework.implem.heuristics.FixedWidth;
import mdd.framework.implem.solver.ParallelSolver;
import org.javagrader.Allow;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.decarbonation.MaximumDecarbonationInstance;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This class tests your complete solution for the Maximum decarbonation problem. If your solution passed all the tests
 * in the `TestMaximumDecarbonationModel` test suite, then you may reasonably assume you have made an error when
 * implementing your relaxation.
 */
@Grade
public final class TestMaximumDecarbonationFast {

    private static List<String> listInstances() throws IOException {
        // Easy set of bruteforceable instances
        final File dir = new File("data/decarbonation/instances");
        return Arrays.stream(dir.listFiles(f -> f.isFile() && f.getName().matches("decarbonation_50_.*")))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    private static int readOptimal(final String path) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(new File(path)))) {
            return Integer.parseInt(r.readLine());
        }
    }

    private static int[] widths() {
        return new int[] {500, 250, 100, 50, 20, 10, 5, 2};
    }

    public static Stream<Arguments> getInstances() throws IOException {
        final List<String> inst  = listInstances();
        final int[] widths       = widths();
        final List<Arguments> out = new ArrayList<>();

        for (int width : widths) {
            for (String name : inst) {
                final String testName = name+ ":: width = " + width;
                out.add(Arguments.arguments(name, width));
            }
        }
        return out.stream();
    }

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @Grade(value = 10)
    @Allow("java.lang.Thread")
    @GradeFeedback(message = "Your solution failed to identify the optimal solution. Is your DP model correct ? If so, chances are you have a bug in your relaxation", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Have you done anything special in your transition/transition cost functions or in the relaxation ?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getInstances")
    public void testOptimality(String instance, int width) throws IOException {
        final String optPath = "data/decarbonation/optimal/" + instance;
        final String instPath= "data/decarbonation/instances/" + instance;
        final int optimal = readOptimal(optPath);
        final MaximumDecarbonationInstance decarbonationInstance = MaximumDecarbonationInstance.fromFile(instPath);
        assertEquals(optimal, solveParametric(decarbonationInstance, width));
    }

    /**
     * This method lets you solve a max decarbonation instance.
     *
     * @param instance the instance you want to solve
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     *
     * @return an optimal solution to the given instance
     */
    public static int solveParametric(final MaximumDecarbonationInstance instance, final int maximumWidth) {
        final MaximumDecarbonationProblem problem = new MaximumDecarbonationProblem(instance);
        final Relaxation<MaximumDecarbonationState> relax = new MaximumDecarbonationRelaxation(instance);
        final StateRanking<MaximumDecarbonationState> ranking = new MaximumDecarbonationStateRanking();
        final VariableHeuristic<MaximumDecarbonationState> varh = new DefaultVariableHeuristic<>();
        final WidthHeuristic<MaximumDecarbonationState> width = new FixedWidth<>(maximumWidth);
        final Frontier<MaximumDecarbonationState> frontier = new SimpleFrontier<>(ranking);

        // let us solve these instances using one thread per cpu core
        // final int threads = Runtime.getRuntime().availableProcessors();
        int threads = 1;
        final Solver solver = new ParallelSolver<>(threads, problem, relax, varh, ranking, width, frontier);
        solver.maximize();

        return solver.bestValue().orElse(Integer.MIN_VALUE).intValue();
    }
}
