package mdd;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;

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

/**
 * This class defines a set of tests that will validate your implementation of
 * the sequential Bab-MDD solver using the provided knapsack model as an example.
 */
@RunWith(Enclosed.class)
public class TestSequentialSolver {
    private static List<File> listInstances(final String path) {
        final File dir = new File(path);
        return Arrays.asList(dir.listFiles(f -> f.isFile() && f.getName().matches("instance_n100_.*")));
    }

    private static int[] widths() {
        return new int[] {1000000, 100000, 1000};
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized100 {
        final String name;
        final Integer width;
        final String instance;

        public TestParameterized100(String name, File f, Integer w) {
            this.name  = name;
            this.width = w;
            this.instance = f.getAbsolutePath();
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> width() {
            final List<File> files   = listInstances("data/Knapsack");
            final int[] widths       = widths();
            final List<Object[]> out = new ArrayList<>();

            for (int width : widths) {
                for (File f : files) {
                    out.add(new Object[]{ f.getName()+ ":: width = " + width, f, width });
                }
            }

            return out;
        }

        @Test(timeout = 6000)
        @Grade(value = 10, cpuTimeout = 2000, customPermissions = DataPermissionFactorySequential.class)
        @GradeFeedback(message = "There is a bug in your SequentialSolver implementation. It failed to retrieve the right solution.", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Are you sure to have correctly used the upper and lower bounds from the relaxed and restricted DDs ?", onTimeout=true)
        public void test() throws IOException {
            KnapsackExample.KnapsackProblem problem = KnapsackExample.readInstance(instance);
            assertEquals(problem.optimal.intValue(), solveParametric(problem, width));
        }
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
