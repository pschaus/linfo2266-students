package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static constraintprogramming.problems.KnightTourAssert.assertInvalid;
import static constraintprogramming.problems.KnightTourAssert.assertValid;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Timeout.ThreadMode.SEPARATE_THREAD;

@Grade(value = 30)
public class KnightTourTest {

    @Test
    @Grade(cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    public void test1() {
        int n = 6;
        int[] moves = new int[]{0, 8, 4, 15, 2, 6, -1, 1, 9, 5, 16, 29, 33, 25, 12, 20, 31, 18, 7, 3, -1, 22, 35, 27, 19, 30, 26, 34, 23, -1, 21, 17, 28, 32, 24, 13};
        KnightTourInstance instance = new KnightTourInstance(n, moves);
        assertValid(instance, 1);
    }


    @Test
    @Grade(cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    public void test2() {
        int n = 6;
        int[] moves = new int[]{0, 8, 4, 15, 2, 6, 14, 1, 12, 25, 33, 29, 16, 5, 9, 17, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        KnightTourInstance instance = new KnightTourInstance(n, moves);
        assertValid(instance, 6);
    }

    @Test
    @Grade(cpuTimeout = 500, unit = MILLISECONDS, threadMode = SEPARATE_THREAD)
    public void test3() {
        int n = 6;
        int[] moves = new int[]{1, 9, 5, 16, 27, 14, 18, 7, 15, 2, 10, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        KnightTourInstance instance = new KnightTourInstance(n, moves);
        assertInvalid(instance);
    }

    static Set<Integer> accessible(int from, int n) {
        Set<Integer> result = new HashSet<>();
        int i = from / n;
        int j = from % n;

        result.add(coord(i+1,j+2, n));
        result.add(coord(i+2,j+1, n));

        result.add(coord(i-1,j+2, n));
        result.add(coord(i-2,j+1, n));

        result.add(coord(i+1,j-2, n));
        result.add(coord(i+2,j-1, n));

        result.add(coord(i-1,j-2, n));
        result.add(coord(i-2,j-1, n));

        return result;
    }

    private static int coord(int i, int j, int n) {
        if (j > n || j < 0 || i > n || i < 0) return -1;
        return i*n+j;
    }


}
