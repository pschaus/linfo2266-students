package linearprogramming;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class FlowMatricesTestFast {

    /**
     * Instance tested:
     * <p>
     * +-15-a----+
     * |         |
     * c-----+   b
     * |     |  /|
     * |     |/  |
     * |    /|   |
     * e---+ +---f
     * |         |
     * +----g-15-+
     * <p>
     * All links have a maximum capacity of value 5, except if explicitly written
     * The source is located at node a and the sink at node g
     * <p>
     * The objective value should be 15
     */
    @Test
    @Grade(value = 10, cpuTimeout = 100, unit = MILLISECONDS)
    public void simpleTest() {
        FlowNetwork network = FlowNetwork.fromFile("data/Flow/example.txt");
        Assertions.assertCorrectness(network);
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

    public static List<Arguments> getNetworkParams() {
        int nVertices = 10;
        int nEdges = 25;
        return IntStream.range(0, 5).mapToObj(i -> arguments(named("n" + nVertices + "_e" + nEdges + "_" + i, nVertices), nEdges)).collect(Collectors.toList());
    }

    @Grade(value = 60, cpuTimeout = 5)
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getNetworkParams")
    public void SmallFlowTest(int vertices, int edges) {
        int source = random.nextInt(vertices);
        int sink = nextDiffExcept(vertices, source);
        FlowNetwork network = new FlowNetwork(vertices, edges, source, sink);
        Assertions.assertCorrectness(network);
    }

}
