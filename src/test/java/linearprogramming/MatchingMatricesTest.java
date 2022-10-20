package linearprogramming;

import com.github.guillaumederval.javagrading.GradeClass;
import com.github.guillaumederval.javagrading.GradingRunnerWithParametersFactory;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.stream.IntStream;


@RunWith(Enclosed.class)
public class MatchingMatricesTest {

    private final static int nTest = 5; // number of parametrized run

    @RunWith(Parameterized.class)
    @Parameterized.UseParametersRunnerFactory(GradingRunnerWithParametersFactory.class)
    @GradeClass(totalValue = 60, defaultCpuTimeout = 7000)
    public static class BigSizeTest {

        Graph bipartite;
        public BigSizeTest(Graph bipartite) {
            this.bipartite = bipartite;
        }

        @Parameterized.Parameters
        public static Object[] data() {
            return IntStream.range(0, nTest).mapToObj(i -> Graph.bipartite(250, 250, 666)).toArray();
        }

        @Test
        public void test() {
            Assertions.assertCorrectness(bipartite);
        }

    }

}
