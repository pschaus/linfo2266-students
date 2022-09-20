package dynamicprogramming;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.Solution;
import util.tsp.TSPInstance;

import java.util.*;

import static org.junit.Assert.assertEquals;


@RunWith(Enclosed.class)
public class TSPTest {

    public static List<Object[]> readTSPInstance(int size) {
        LinkedList<Object []> coll = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            String name = "data/TSP/instance_"+size+"_"+i+".xml";
            coll.add(new Object[] {name, new TSPInstance(name)});
        }
        return coll;
    }

    public static void testSolvingOptimality(TSPInstance instance) {
        TSP model = new TSP(instance);
        DynamicProgramming<TSPState> solver = new DynamicProgramming<>(model);
        Solution solution = solver.getSolution();
        
        assertEquals(instance.objective, solution.getValue(), 1e-3);

        double checkValue = 0;
        int position = 0;
        for (int decision : solution.getDecisions()) {
            checkValue += instance.distanceMatrix[position][decision];
            position = decision;
        }
        checkValue += instance.distanceMatrix[position][0]; // back to initial position

        assertEquals(instance.objective, checkValue, 1e-3);
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized8 {
        final TSPInstance instance;
        public TestParameterized8(String name, TSPInstance instance) {
            this.instance = instance;
        }
        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return readTSPInstance(8);
        }
        @Test
        @Grade(value = 10, cpuTimeout = 1000)
        @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle minimization properly?", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", onTimeout=true)
        public void test() throws Exception {
            testSolvingOptimality(instance);
        }
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized10 {
        final TSPInstance instance;
        public TestParameterized10(String name, TSPInstance instance) {
            this.instance = instance;
        }
        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return readTSPInstance(10);
        }
        @Test
        @Grade(value = 10, cpuTimeout = 1000)
        @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle minimization properly?", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", onTimeout=true)
        public void test() throws Exception {
            testSolvingOptimality(instance);
        }
    }

    /*
    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized18 {
        final TSPInstance instance;
        public TestParameterized18(String name, TSPInstance instance) {
            this.instance = instance;
        }
        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return readTSPInstance(18);
        }
        @Test
        @Grade(value = 10, cpuTimeout = 5000)
        @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle minimization properly?", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", onTimeout=true)
        public void test() throws Exception {
            testSolvingOptimality(instance);
        }
    }
     */

}
