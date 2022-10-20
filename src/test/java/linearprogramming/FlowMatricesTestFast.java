package linearprogramming;

import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Random;
import java.util.stream.IntStream;

@RunWith(Enclosed.class)
public class FlowMatricesTestFast {

    /**
     * Instance tested:
     *
     *        +-15-a----+
     *        |         |
     *        c-----+   b
     *        |     |  /|
     *        |     |/  |
     *        |    /|   |
     *        e---+ +---f
     *        |         |
     *        +----g-15-+
     *
     * All links have a maximum capacity of value 5, except if explicitly written
     * The source is located at node a and the sink at node g
     *
     * The objective value should be 15
     */
    @GradeClass(totalValue = 10, defaultCpuTimeout = 100)
    public static class SimpleTest {

        @Test
        public void simpleTest() {
            FlowNetwork network = FlowNetwork.fromFile("data/Flow/example.txt");
            Assertions.assertCorrectness(network);
        }

    }

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
    @GradeClass(totalValue = 30, defaultCpuTimeout = 100)
    public static class SmallFlowTest {

        FlowNetwork flowNetwork;
        public SmallFlowTest(FlowNetwork flowNetwork) {
            this.flowNetwork = flowNetwork;
        }

        @Parameterized.Parameters
        public static Object[] data() {
            int V = 20;
            return IntStream.range(0, nTest)
                    .map(i -> nextInt(V))
                    .mapToObj(i -> new FlowNetwork(V, 50, i, nextDiffExcept(V, i))).toArray();
        }

        @Test
        public void test() {
            Assertions.assertCorrectness(flowNetwork);
        }

    }

}
