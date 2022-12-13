package mdd;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;
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
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.decarbonation.MaximumDecarbonationInstance;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * This class tests your complete solution for the Maximum decarbonation problem. If your solution passed all the tests
 * in the `TestMaximumDecarbonationModel` test suite, then you may reasonably assume you have made an error when
 * implementing your relaxation.
 */
@RunWith(Enclosed.class)
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

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized {
        final String name;
        final String instance;
        final Integer width;

        public TestParameterized(String name, String instance, Integer w) {
            this.name  = name;
            this.instance = instance;
            this.width = w;
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> width() throws IOException {
            final List<String> inst  = listInstances();
            final int[] widths       = widths();
            final List<Object[]> out = new ArrayList<>();

            for (int width : widths) {
                for (String name : inst) {
                    final String testName = name+ ":: width = " + width;
                    out.add(new Object[]{testName , name, width });
                }
            }

            return out;
        }

        @Test(timeout = 6000)
        @Grade(value = 10, cpuTimeout = 2000, customPermissions = DataPermissionFactoryWithThreads.class)
        @GradeFeedback(message = "Your solution failed to identify the optimal solution. Is your DP model correct ? If so, chances are you have a bug in your relaxation", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Have you done anything special in your transition/transition cost functions or in the relaxation ?", onTimeout=true)
        public void test() throws IOException {
            final String optPath = "data/decarbonation/optimal/" + instance;
            final String instPath= "data/decarbonation/instances/" + instance;
            final int optimal = readOptimal(optPath);
            final MaximumDecarbonationInstance instance = MaximumDecarbonationInstance.fromFile(instPath);
            assertEquals(optimal, solveParametric(instance, width));
        }
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
        final int cores = Runtime.getRuntime().availableProcessors();
        final Solver solver = new ParallelSolver<>(cores, problem, relax, varh, ranking, width, frontier);
        solver.maximize();

        return solver.bestValue().orElse(Integer.MIN_VALUE).intValue();
    }
}
