package dynamicprogramming;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;

import util.Solution;
import util.knapsack.KnapsackInstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class KnapsackTest {

    public static List<Object[]> readKnapsackInstance(int size) {
        LinkedList<Object []> coll = new LinkedList<>();

        File instanceDir = new File("data/Knapsack");
        File[] instances = instanceDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("instance_n" + size + "_");
            }
        });

        for (File instance : instances) {
            coll.add(new Object[] { instance.getName(), new KnapsackInstance(instance.getAbsolutePath()) });
        }
        return coll;
    }

    public static void testSolvingOptimality(KnapsackInstance instance) {
        Knapsack model = new Knapsack(instance);
        DynamicProgramming<KnapsackState> solver = new DynamicProgramming<>(model);
        Solution solution = solver.getSolution();
        
        assertEquals(instance.objective, (int) solution.getValue());

        int checkValue = 0, checkWeight = 0, item = 0;
        for (int decision : solution.getDecisions()) {
            if (decision == 1) {
                checkValue += instance.value[item];
                checkWeight += instance.weight[item];
            }

            item++;
        }

        assertEquals(instance.objective, checkValue);
        assertTrue(checkWeight <= instance.capacity);
    }

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized100 {
        final KnapsackInstance instance;
        public TestParameterized100(String name, KnapsackInstance instance) {
            this.instance = instance;
        }
        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return readKnapsackInstance(100);
        }
        @Test
        @Grade(value = 10, cpuTimeout = 2000)
        @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle maximization properly?", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", onTimeout=true)
        public void test() throws Exception {
            testSolvingOptimality(instance);
        }
    }

    /*
    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    public static class TestParameterized1000 {
        final KnapsackInstance instance;
        public TestParameterized1000(String name, KnapsackInstance instance) {
            this.instance = instance;
        }
        @Parameterized.Parameters(name = "{0}")
        public static Collection<?> data() {
            return readKnapsackInstance(1000);
        }
        @Test
        @Grade(value = 10, cpuTimeout = 5000)
        @GradeFeedback(message = "Something in your DP model might be wrong. Do you handle maximization properly?", onFail=true)
        @GradeFeedback(message = "Your solver is too slow. Are you using the DP table? Is your state definition correct?", onTimeout=true)
        public void test() throws Exception {
            testSolvingOptimality(instance);
        }
    }*/
    
}
