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

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class FlowMatricesTest {

    static Random random = new Random();

    private static int nextDiffExcept(int bound, int forbidden) {
        int candidate = random.nextInt(bound);
        while (candidate == forbidden)
            candidate = random.nextInt(bound);
        return candidate;
    }

    public static List<Arguments> getNetworkParams() {
        int nVertices = 300;
        int nEdges = 1000;
        return IntStream.range(0, 5).mapToObj(i -> arguments(named("n" + nVertices + "_e" + nEdges + "_" + i, nVertices), nEdges)).collect(Collectors.toList());
    }

    @Grade(value = 60, cpuTimeout = 8)
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getNetworkParams")
    public void BigFlowTest(int vertices, int edges) {
        // and not the 'Oli' test
        int source = random.nextInt(vertices);
        int sink = nextDiffExcept(vertices, source);
        FlowNetwork network = new FlowNetwork(vertices, edges, source, sink);
        Assertions.assertCorrectness(network);
    }

}
