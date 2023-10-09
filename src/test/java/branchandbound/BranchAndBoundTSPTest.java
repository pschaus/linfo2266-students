package branchandbound;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import util.tsp.TSPInstance;

import java.util.*;

import static org.javagrader.TestResultStatus.FAIL;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Grade
public class BranchAndBoundTSPTest {

    @Test
    @Grade(value = 1, cpuTimeout = 1)
    @GradeFeedback(message = "Sorry, TSP not working yet", on = FAIL)
    public void readableTestToDebug() {
        // https://pasteboard.co/0Af7efOavbEo.png (use this link to visualize the instance and debug)
        int [] xCoord = new int[] {10,10,20,70,40,60,50};
        int [] yCoord = new int[] {10,30,60,40,10,20,60};
        TSPInstance tsp = new TSPInstance(xCoord,yCoord);
        List<Edge> edges;

        boolean [][] expectedEdges = new boolean[7][7];
        // 0-1, 1-2, 2-6, 6-3, 3-5, 5-4, 4-0
        expectedEdges[0][1] = expectedEdges[1][0] = true;
        expectedEdges[1][2] = expectedEdges[2][1] = true;
        expectedEdges[2][6] = expectedEdges[6][2] = true;
        expectedEdges[6][3] = expectedEdges[3][6] = true;
        expectedEdges[3][5] = expectedEdges[5][3] = true;
        expectedEdges[5][4] = expectedEdges[4][5] = true;
        expectedEdges[4][0] = expectedEdges[0][4] = true;

        OneTreeLowerBound [] algos = new OneTreeLowerBound[] {new SimpleOneTree(), new HeldKarpOneTree()};

        for (OneTreeLowerBound algo: algos) {
            edges = BranchAndBoundTSP.optimize(tsp,algo);
            assertEquals(7, edges.size());
            boolean [][] foundEdges = new boolean[7][7];
            for (Edge e: edges) {
                foundEdges[e.v1()][e.v2()] = foundEdges[e.v2()][e.v1()] = true;
            }
            for (int i = 0; i < expectedEdges.length; i++) {
                for (int j = 0; j < expectedEdges.length; j++) {
                    assertEquals(expectedEdges[i][j], foundEdges[i][j]);
                }
            }
        }
    }

    public static List<Arguments> getTSPInstances10() {
        return readTSPInstances(10);
    }

    public static List<Arguments> getTSPInstances20() {
        return readTSPInstances(20);
    }

    public static List<Arguments> readTSPInstances(int size) {
        LinkedList<Arguments> coll = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            String name = "data/TSP/instance_"+size+"_"+i+".xml";
            String features = "n" + size + "_" + i;
            coll.add(arguments(named(features, new TSPInstance(name))));
        }
        return coll;
    }

    public static void testSolvingOptimality(TSPInstance instance) {
        List<Edge> edges = BranchAndBoundTSP.optimize(instance,new HeldKarpOneTree());
        int objective = instance.objective;
        double tourLength = 0;
        for (Edge e: edges) {
            tourLength += instance.distanceMatrix[e.v1()][e.v2()];
        }
        assertEquals(objective, tourLength, 0.001);
    }

    @Grade(value = 10, cpuTimeout = 2)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances10")
    public void testOptimality10(TSPInstance instance) {
        testSolvingOptimality(instance);
    }

    @Grade(value = 5, cpuTimeout = 4)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @MethodSource("getTSPInstances20")
    public void testOptimality20(TSPInstance instance) {
        testSolvingOptimality(instance);
    }

    @Grade(value = 5, cpuTimeout = 20)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @CsvSource({
            "30_3",
    })
    public void testOptimality30_3(String filename) {
        TSPInstance instance = new TSPInstance("data/TSP/instance_"+filename+".xml");
        testSolvingOptimality(instance);
    }

    @Grade(value = 5, cpuTimeout = 5)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @CsvSource({
            "30_1",
    })
    public void testOptimality30_1(String filename) {
        TSPInstance instance = new TSPInstance("data/TSP/instance_"+filename+".xml");
        testSolvingOptimality(instance);
    }

    @Grade(value = 5, cpuTimeout = 2)
    @GradeFeedback(message = "Sorry, something is wrong cannot find optimum", on = FAIL)
    @GradeFeedback(message = "Your solver is too slow", on = TIMEOUT)
    @ParameterizedTest
    @CsvSource({
            "30_0",
            "30_2",
            "30_4",
            "30_5",
            "30_6",
            "30_7",
            "30_8",
            "30_9",
    })
    public void testOptimality30(String filename) {
        TSPInstance instance = new TSPInstance("data/TSP/instance_"+filename+".xml");
        testSolvingOptimality(instance);
    }

}
