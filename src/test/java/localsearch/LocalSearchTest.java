package localsearch;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.javagrader.Allow;
import org.javagrader.ConditionalOrderingExtension;
import org.javagrader.CustomGradingResult;
import org.javagrader.Grade;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Grade(value = 9)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Allow("all")
public class LocalSearchTest {

    private static boolean timeoutEnsured = true;
    private static boolean notImplemented = false;

    public static void testInstance(PSPInstance instance, int timeLimit) throws CustomGradingResult {
        int solution = 0;

        try {
            PSP problem = new PSP(instance);

            LocalSearch localSearch = new LocalSearch(problem);
            long initTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            Candidate candidate = localSearch.solve(timeLimit);
            long elapsedTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            assertTrue(elapsedTime - initTime > 1000000000L * 0.9 * timeLimit, "You need to use the CPU time limit provided");
            assertTrue(elapsedTime - initTime < 1000000000L * 1.1 * timeLimit, "You should not exceed the CPU time limit provided");

            Utils.assertFeasible(instance, candidate);

            solution = candidate.getValue();
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (Exception | Error e) {
            throw new CustomGradingResult(FAIL, 0, e.getMessage());
        }

        if (solution == instance.objective) {
            throw new CustomGradingResult(SUCCESS, 110, "You found the optimal solution!");
        } else if (solution <= 1.05 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 100, "Your solution is within 5% of the optimal solution");
        } else if (solution <= 1.1 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 90, "Your solution is within 5-10% of the optimal solution");
        } else if (solution <= 1.2 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 80, "Your solution is within 10-20% of the optimal solution");
        } else if (solution <= 1.4 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 50, "Your solution is within 20-40% of the optimal solution");
        } else {
            throw new CustomGradingResult(SUCCESS, 0, "Your solution is not within 40% of the optimal solution");
        }
    }

    public static List<Arguments> getSmallInstances() {
        return Utils.readPSPInstances("data/PSP/small");
    }

    public static List<Arguments> getMediumInstances() {
        return Utils.readPSPInstances("data/PSP/medium");
    }

    /**
     * Test that the timeout is ensured and that a somewhat interesting solution is returned
     * @param instance instance to test
     */
    @Order(1)
    @Grade(value = 25)
    @ParameterizedTest
    @MethodSource("getSmallInstances")
    public void testTimeoutEnsured(PSPInstance instance) {
        try {
            preConditions();
            assertTimeoutPreemptively(Duration.ofMillis(1500), () -> {
                PSP problem = new PSP(instance);
                LocalSearch localSearch = new LocalSearch(problem);
                long initTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
                Candidate candidate = localSearch.solve(1);
                long elapsedTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
                assertTrue(elapsedTime - initTime > 1000000000L * 0.95, "You need to use the CPU time limit provided");

                Utils.assertFeasible(instance, candidate);

                int solution = candidate.getValue();
                assertTrue(solution <= 2.5 * instance.objective);
            });
        } catch (NotImplementedException e) {
            notImplemented = true;
            NotImplementedExceptionAssume.fail(e);
        } catch (AssertionFailedError e) {
            if (e.getMessage().contains("timed out"))
                timeoutEnsured = false;
            throw e;
        }
    }

    private static void preConditions() {
        if (notImplemented) {
            NotImplementedExceptionAssume.fail(new NotImplementedException());
        } else if (!timeoutEnsured) {
            throw new RuntimeException("Timeout is not respected");
        }
    }

    @Order(2)
    @Grade(value = 100, custom = true, cpuTimeout = 44, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @ParameterizedTest
    @MethodSource("getSmallInstances")
    public void testSmallInstances(PSPInstance instance) throws CustomGradingResult {
        preConditions();
        int timeLimit = 40;
        testInstance(instance, timeLimit);
    }

    @Order(3)
    @Grade(value = 100, custom = true, cpuTimeout = 88, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @ParameterizedTest
    @MethodSource("getMediumInstances")
    public void testMediumInstances(PSPInstance instance) throws CustomGradingResult {
        preConditions();
        int timeLimit = 80;
        testInstance(instance, timeLimit);
    }

}
