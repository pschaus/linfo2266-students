package mdd;

import mdd.exercise.*;
import mdd.framework.core.*;
import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.javagrader.TestResultStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.decarbonation.MaximumDecarbonationInstance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


/**
 * This class tests your **DP model** for the Maximum decarbonation instance. You might want to make sure you
 * have all the tests passing in this class before attempting to solve the other parts of the assignment on
 * max decarbonation.
 */
@Grade
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

    public static Stream<Arguments> getInstances() throws IOException {
        final List<String> inst  = listInstances();
        final List<Arguments> out = new ArrayList<>();

        for (String name : inst) {
            final String testName = name+ ":: pure dp";
            out.add(arguments(name));
        }

        return out.stream();
    }

    @Grade(value = 10, cpuTimeout = 1)
    @GradeFeedback(message = "Something in your DP model might be wrong. It failed to identify the optimal solution", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow. Have you done anything special in your transition/transition cost functions ?", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getInstances")
    public void testModel(String instance) throws IOException {
        final String optPath = "data/decarbonation/optimal/" + instance;
        final String instPath= "data/decarbonation/instances/" + instance;
        final int optimal = readOptimal(optPath);
        final MaximumDecarbonationInstance decarbonationInstance = MaximumDecarbonationInstance.fromFile(instPath);
        assertEquals(optimal, solvePureDp(decarbonationInstance));
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
