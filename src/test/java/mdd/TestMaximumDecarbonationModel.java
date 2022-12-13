package mdd;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;
import mdd.exercise.*;
import mdd.framework.core.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import util.decarbonation.MaximumDecarbonationInstance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * This class tests your **DP model** for the Maximum decarbonation instance. You might want to make sure you
 * have all the tests passing in this class before attempting to solve the other parts of the assignment on
 * max decarbonation.
 */
@RunWith(Enclosed.class)
public final class TestMaximumDecarbonationModel {
    private static List<String> listInstances() throws IOException {
        // Easy set of bruteforceable instances
        final File dir = new File("data/decarbonation/instances");
        return Stream.concat(
                    Stream.concat(
                        Arrays.stream(dir.listFiles(f -> f.isFile() && f.getName().matches("decarbonation_10_.*"))).limit(10),
                        Arrays.stream(dir.listFiles(f -> f.isFile() && f.getName().matches("decarbonation_50_.*")))).limit(10),
                        Arrays.stream(dir.listFiles(f -> f.isFile() && f.getName().matches("decarbonation_100_.*"))).limit(2))
                    .map(File::getName)
                    .collect(Collectors.toList());
    }

    private static int readOptimal(final String path) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(new File(path)))) {
            return Integer.parseInt(r.readLine());
        }
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized {
        final String name;
        final String instance;

        public TestParameterized(String name, String instance) {
            this.name  = name;
            this.instance = instance;
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> width() throws IOException {
            final List<String> inst  = listInstances();
            final List<Object[]> out = new ArrayList<>();

            for (String name : inst) {
                final String testName = name+ ":: pure dp";
                out.add(new Object[]{testName , name});
            }

            return out;
        }

        @Test(timeout = 3000)
        @Grade(value = 10, cpuTimeout = 1000, customPermissions = DataPermissionFactorySequential.class)
        @GradeFeedback(message = "Something in your DP model might be wrong. It failed to identify the optimal solution", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Have you done anything special in your transition/transition cost functions ?", onTimeout=true)
        public void test() throws IOException {
            final String optPath = "data/decarbonation/optimal/" + instance;
            final String instPath= "data/decarbonation/instances/" + instance;
            final int optimal = readOptimal(optPath);
            final MaximumDecarbonationInstance instance = MaximumDecarbonationInstance.fromFile(instPath);
            assertEquals(optimal, solvePureDp(instance));
        }
    }

    public static int solvePureDp(final MaximumDecarbonationInstance instance) {
        final MaximumDecarbonationProblem problem = new MaximumDecarbonationProblem(instance);
        final HashMap<MaximumDecarbonationState, Integer> cache = new HashMap<>();
        return pureDp(problem, problem.initialState(),0, cache);
    }

    private static int pureDp(final MaximumDecarbonationProblem problem,
                              final MaximumDecarbonationState state,
                              final int depth,
                              final HashMap<MaximumDecarbonationState, Integer> cache) {
        if (depth == problem.nbVars()) {
            return problem.initialValue();
        } else if (cache.containsKey(state)) {
            return cache.get(state);
        } else {
            int max = Integer.MIN_VALUE;
            Iterator<Integer> domain = problem.domain(state, depth);
            while (domain.hasNext()) {
                final Decision decision = new Decision(depth, domain.next());
                final MaximumDecarbonationState next = problem.transition(state, decision);
                final int cost = problem.transitionCost(state, decision);
                final int sol  = cost + pureDp(problem, next, depth + 1, cache);
                if (sol > max) {
                    max = sol;
                }
            }
            if (!cache.containsKey(state)) {
                cache.put(state, max);
            }
            return max;
        }
    }
}
