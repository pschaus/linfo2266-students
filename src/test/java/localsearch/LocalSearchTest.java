package localsearch;

import com.github.guillaumederval.javagrading.CustomGradingResult;
import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;
import com.github.guillaumederval.javagrading.TestStatus;

import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import util.psp.PSPInstance;

@RunWith(Enclosed.class)
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
        } catch (AssertionError error) {
            throw new CustomGradingResult(TestStatus.FAILED, 0, error.getMessage());
        } catch (Exception e) {
            throw new CustomGradingResult(TestStatus.FAILED, 0);
        }

        if (solution == instance.objective) {
            throw new CustomGradingResult(TestStatus.SUCCESS, 1.1 * value, "You found the optimal solution!");
        } else if (solution <= 1.05 * instance.objective) {
            throw new CustomGradingResult(TestStatus.SUCCESS, value, "Your solution is within 5% of the optimal solution");
        } else if (solution <= 1.1 * instance.objective) {
            throw new CustomGradingResult(TestStatus.SUCCESS, 0.9 * value, "Your solution is within 5-10% of the optimal solution");
        } else if (solution <= 1.2 * instance.objective) {
            throw new CustomGradingResult(TestStatus.SUCCESS, 0.8 * value, "Your solution is within 10-20% of the optimal solution");
        } else if (solution <= 1.4 * instance.objective) {
            throw new CustomGradingResult(TestStatus.SUCCESS, 0.5 * value, "Your solution is within 20-40% of the optimal solution");
        } else {
            throw new CustomGradingResult(TestStatus.SUCCESS, 0, "Your solution is not within 40% of the optimal solution");
        }
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestSmall {
        static final int timeLimit = 40;
        final PSPInstance instance;

        public TestSmall(String name, PSPInstance instance) {
            this.instance = instance;
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return Utils.readPSPInstances("data/PSP/small");
        }

        @Test(timeout = timeLimit * 3000)
        @Grade(value = value, cpuTimeout = timeLimit * 1100, custom = true)
        public void testLocalSearch() throws Exception {
            testInstance(instance, timeLimit);
        }
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestMedium {
        static final int timeLimit = 80;
        final PSPInstance instance;

        public TestMedium(String name, PSPInstance instance) {
            this.instance = instance;
        }

        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return Utils.readPSPInstances("data/PSP/medium");
        }

        @Test(timeout = timeLimit * 3000)
        @Grade(value = value, cpuTimeout = timeLimit * 1100, custom = true)
        public void testLocalSearch() throws Exception {
            testInstance(instance, timeLimit);
        }
    }
}
