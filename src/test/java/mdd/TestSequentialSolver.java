package mdd;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import mdd.exercise.KnapsackExample;
import mdd.framework.core.Frontier;
import mdd.framework.core.Relaxation;
import mdd.framework.core.Solver;
import mdd.framework.heuristics.StateRanking;
import mdd.framework.heuristics.VariableHeuristic;
import mdd.framework.heuristics.WidthHeuristic;
import mdd.framework.implem.frontier.SimpleFrontier;
import mdd.framework.implem.heuristics.DefaultVariableHeuristic;
import mdd.framework.implem.heuristics.FixedWidth;
import mdd.framework.implem.solver.SequentialSolver;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * This class defines a set of tests that will validate your implementation of
 * the sequential Bab-MDD solver using the provided knapsack model as an example.
 */
@Grade
public class TestSequentialSolver {
    private static List<File> listInstances(final String path) {
        final File dir = new File(path);
        return Arrays.asList(dir.listFiles(f -> f.isFile() && f.getName().matches("instance_n100_.*")));
    }

    private static int[] widths() {
        return new int[] {1000000, 100000, 1000};
    }

    public static Stream<Arguments> getInstances() {
        final List<File> files   = listInstances("data/Knapsack");
        final int[] widths       = widths();
        final List<Arguments> out = new ArrayList<>();

        for (int width : widths) {
            for (File f : files) {
                String name = f.getName().replace("instance_", "")+ " :: width=1e" + (int) Math.log10(width);
                out.add(arguments(named(name, f.getPath()), width));
            }
        }

        return out.stream();
    }

    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "There is a bug in your SequentialSolver implementation. It failed to retrieve the right solution.", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Are you sure to have correctly used the upper and lower bounds from the relaxed and restricted DDs ?", on = TIMEOUT)
    @ParameterizedTest(name = "{0}")
    @MethodSource("getInstances")
    public void testKnapsack(String instance, int width) throws IOException {
        KnapsackExample.KnapsackProblem problem = KnapsackExample.readInstance(instance);
        assertEquals(problem.optimal.intValue(), solveParametric(problem, width));
    }


    /**
     * This method lets you solve a knapsack instance.
     *
     * @param problem the problem you want to solve
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     *
     * @return an optimal solution to the given instance
     */
    public static int solveParametric(final KnapsackExample.KnapsackProblem problem, final int maximumWidth) throws IOException {
        final Relaxation<Integer>       relax = new KnapsackExample.KnapsackRelax();
        final StateRanking<Integer>   ranking = new KnapsackExample.KnapsackRanking();
        final VariableHeuristic<Integer> varh = new DefaultVariableHeuristic<>();
        final WidthHeuristic<Integer>   width = new FixedWidth<>(maximumWidth);
        final Frontier<Integer>      frontier = new SimpleFrontier<>(ranking);
        final Solver                   solver = new SequentialSolver<>(problem, relax, varh, ranking, width, frontier);
        solver.maximize();

        return solver.bestValue().orElse(Integer.MAX_VALUE).intValue();
    }
}
