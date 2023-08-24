package constraintprogramming.problems;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static constraintprogramming.problems.MagicSquareAssert.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;


@Grade(value = 20)
public class MagicSquareTestFast {

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void test1x1() {
        assertValid(new int[1][1], 1);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void test2x2() {
        assertInvalid(new int[2][2]);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void test3x3WithInitialValues1() {
        int[][] values = new int[][] {
                {2, 0, 6},
                {0, 5, 1},
                {0, 3, 0},
        };
        assertValid(values, 1);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void test3x3WithInitialValues2() {
        int[][] values = new int[][] {
                {6, 0, 0},
                {1, 0, 0},
                {0, 0, 0},
        };
        assertValid(values, 1);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void test3x3WithInitialValues3() {
        int[][] values = new int[][]{
                {0, 0, 0},
                {9, 5, 1},
                {0, 0, 0},
        };
        assertValid(values, 2);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void test3x3WithoutInitialValues() {
        assertValid(new int[3][3], 8);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void testInvalid1() {
        int[][] values = new int[][] {
                {1, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 1},
        };
        assertInvalid(values);
    }

    @Test
    @Grade(cpuTimeout = 200, unit = MILLISECONDS)
    public void testInvalid2() {
        int[][] values = new int[][] {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {2, 1, 3, 0},
        };
        assertInvalid(values);
    }

}
