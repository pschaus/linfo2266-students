package linearprogramming;

import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Random;
import java.util.stream.IntStream;

@RunWith(Enclosed.class)
public class FlowMatricesTest {

    private final static int nTest = 5; // number of parametrized run

    static Random random = new Random();
    private static int nextDiffExcept(int bound, int forbidden) {
        int candidate = random.nextInt(bound);
        while (candidate == forbidden)
            candidate = random.nextInt(bound);
        return candidate;
    }
    private static int nextInt(int bound) {
        return random.nextInt(bound);
    }


    @RunWith(Parameterized.class)
    @GradeClass(totalValue = 60, defaultCpuTimeout = 5000)
    public static class BigFlowTest {
        // and not the 'Oli' test

        FlowNetwork flowNetwork;
        public BigFlowTest(FlowNetwork flowNetwork) {
            this.flowNetwork = flowNetwork;
        }

        @Parameterized.Parameters
        public static Object[] data() {
            int V = 300;
            int E = 1000;
            return IntStream.range(0, nTest)
                    .map(i -> nextInt(V))
                    .mapToObj(i -> new FlowNetwork(V, E, i, nextDiffExcept(V, i))).toArray();
        }

        @Test
        public void test() {
            Assertions.assertCorrectness(flowNetwork);
        }

    }

}
