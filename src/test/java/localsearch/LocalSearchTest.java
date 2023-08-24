package localsearch;

import java.util.Collection;
import java.util.List;

import org.javagrader.CustomGradingResult;
import org.javagrader.Grade;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.SUCCESS;

@Grade
public class LocalSearchTest {

    static final int value = 3;

    public static void testInstance(PSPInstance instance, int timeLimit) throws CustomGradingResult {
        int solution = 0;

        try {
            PSP problem = new PSP(instance);

            LocalSearch localSearch = new LocalSearch(problem);
            Candidate candidate = localSearch.solve(timeLimit);

            Utils.assertFeasible(instance, candidate);

            solution = candidate.getValue();
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (Exception | Error e) {
            throw new CustomGradingResult(FAIL, 0, e.getMessage());
        }

        if (solution == instance.objective) {
            throw new CustomGradingResult(SUCCESS, 1.1 * value, "You found the optimal solution!");
        } else if (solution <= 1.05 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, value, "Your solution is within 5% of the optimal solution");
        } else if (solution <= 1.1 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 0.9 * value, "Your solution is within 5-10% of the optimal solution");
        } else if (solution <= 1.2 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 0.8 * value, "Your solution is within 10-20% of the optimal solution");
        } else if (solution <= 1.4 * instance.objective) {
            throw new CustomGradingResult(SUCCESS, 0.5 * value, "Your solution is within 20-40% of the optimal solution");
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

    @Grade(value = 3, custom = true, cpuTimeout = 44)
    @ParameterizedTest
    @MethodSource("getSmallInstances")
    public void testSmallInstances(PSPInstance instance) throws CustomGradingResult {
        int timeLimit = 40;
        testInstance(instance, timeLimit);
    }

    @Grade(value = 3, custom = true, cpuTimeout = 88)
    @ParameterizedTest
    @MethodSource("getMediumInstances")
    public void testMediumInstances(PSPInstance instance) throws CustomGradingResult {
        int timeLimit = 80;
        testInstance(instance, timeLimit);
    }

}
