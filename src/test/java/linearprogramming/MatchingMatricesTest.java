package linearprogramming;

import org.javagrader.Grade;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class MatchingMatricesTest {

    public static List<Arguments> getGraphParams() {
        int nVertices = 199;
        int nEdges = 430;
        return IntStream.range(0, 5).mapToObj(i -> arguments(named("n" + nVertices + "_e" + nEdges + "_" + i, nVertices), nEdges)).collect(Collectors.toList());
    }

    @Grade(value = 60, cpuTimeout = 16)
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getGraphParams")
    public void bigSizeTest(int vertices, int edges) {
        Graph bipartite = Graph.bipartite(vertices, vertices, edges);
        Assertions.assertCorrectness(bipartite);
    }

}
