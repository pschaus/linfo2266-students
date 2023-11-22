package constraintprogramming.problems;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Random;

import static constraintprogramming.problems.MagicSquareAssert.assertValid;
import static org.javagrader.TestResultStatus.TIMEOUT;
import static org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD;


@Grade(value = 30)
public class MagicSquareTest {

    @Test
    @Grade(cpuTimeout = 2, threadMode = SEPARATE_THREAD)
    @GradeFeedback(message = "Did you set and use the value of the magic constant (getMagicConstant)?", on = TIMEOUT)
    public void test4x4() {
        int[][] values = new int[][] {
                {1, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
        };
        assertValid(values, 416);
    }

    @Test
    @Grade(cpuTimeout = 4, threadMode = SEPARATE_THREAD)
    @GradeFeedback(message = "Did you set and use the value of the magic constant (getMagicConstant)?", on = TIMEOUT)
    public void test5x5() {
        int[][] values = new int[][] {
                {5, 0, 0, 0, 1},
                {0, 0, 0, 0, 0},
                {0, 8, 0, 7, 0},
                {0, 0, 9, 0, 0},
                {0, 6, 2, 0, 0},
        };
        assertValid(values, 16);
    }

    @Test
    @Grade(cpuTimeout = 4, threadMode = SEPARATE_THREAD)
    public void testIncremental() {
        int[][] values = new int[][] {
                {7,  0,  35, 0,  0,  14},
                {0,  5,  0,  0,  16, 0 },
                {0,  0,  19, 18, 11, 0 },
                {25, 0,  20, 17, 0,  0 },
                {0,  22, 0,  0,  31, 30},
                {21, 24, 0,  0,  29, 32},
        };
        int[] nSols = new int[] {2, 2, 2, 2, 8};
        assertValid(values, nSols[0]);
        Random random = new Random(666);
        int i = 0;
        int j = 1;
        int nIter = nSols.length;
        for (int k = 1; k < nIter ; ++k) {
            while (values[i][j] == 0) {
                i = random.nextInt(6);
                j = random.nextInt(6);
            }
            values[i][j] = 0;
            assertValid(values, nSols[k]);
        }
    }


}
