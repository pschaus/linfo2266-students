package constraintprogramming.problems;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;

import static constraintprogramming.problems.KillerSudokuAssert.assertInvalid;
import static constraintprogramming.problems.KillerSudokuAssert.assertValid;

@GradeClass(totalValue = 30)
public class KillerSudokuTest {

    @Test(timeout = 1500)
    @Grade(cpuTimeout = 500)
    public void test1() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(3,  coords[0][0], coords[0][1]),
                new KillerSudokuGroup(15,  coords[0][2], coords[0][3], coords[0][4]),
                new KillerSudokuGroup(22,  coords[0][5], coords[1][4], coords[1][5], coords[2][4]),
                new KillerSudokuGroup(4,  coords[0][6], coords[1][6]),
                new KillerSudokuGroup(16,  coords[0][7], coords[1][7]),
                new KillerSudokuGroup(15,  coords[0][8], coords[1][8], coords[2][8], coords[3][8]),

                new KillerSudokuGroup(25,  coords[1][0], coords[1][1], coords[2][0], coords[2][1]),
                new KillerSudokuGroup(17,  coords[1][2], coords[1][3]),

                new KillerSudokuGroup(9,  coords[2][2], coords[2][3], coords[3][3]),
                new KillerSudokuGroup(8,  coords[2][5], coords[3][5], coords[4][5]),
                new KillerSudokuGroup(20,  coords[2][6], coords[2][7], coords[3][6]),

                new KillerSudokuGroup(6,  coords[3][0], coords[4][0]),
                new KillerSudokuGroup(14,  coords[3][1], coords[3][2]),
                new KillerSudokuGroup(17,  coords[3][4], coords[4][4], coords[5][4]),
                new KillerSudokuGroup(17,  coords[3][7], coords[4][6], coords[4][7]),

                new KillerSudokuGroup(13,  coords[4][1], coords[4][2], coords[5][1]),
                new KillerSudokuGroup(20,  coords[4][3], coords[5][3], coords[6][3]),
                new KillerSudokuGroup(12,  coords[4][8], coords[5][8]),

                new KillerSudokuGroup(27,  coords[5][0], coords[6][0], coords[7][0], coords[8][0]),
                new KillerSudokuGroup(6,  coords[5][2], coords[6][1], coords[6][2]),
                new KillerSudokuGroup(20,  coords[5][5], coords[6][5], coords[6][6]),
                new KillerSudokuGroup(6,  coords[5][6], coords[5][7]),

                new KillerSudokuGroup(10,  coords[6][4], coords[7][3], coords[7][4], coords[8][3]),
                new KillerSudokuGroup(14,  coords[6][7], coords[6][8], coords[7][7], coords[7][8]),

                new KillerSudokuGroup(8,  coords[7][1], coords[8][1]),
                new KillerSudokuGroup(16,  coords[7][2], coords[8][2]),
                new KillerSudokuGroup(15,  coords[7][5], coords[7][6]),

                new KillerSudokuGroup(13,  coords[8][4], coords[8][5], coords[8][6]),
                new KillerSudokuGroup(17,  coords[8][7], coords[8][8]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertValid(1, instance);
    }

    @Test(timeout = 1500)
    @Grade(cpuTimeout = 500)
    public void test2() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(3,  coords[0][0], coords[0][1]),
                new KillerSudokuGroup(15,  coords[0][2], coords[0][3], coords[0][4]),
                new KillerSudokuGroup(22,  coords[0][5], coords[1][4], coords[1][5], coords[2][4]),
                new KillerSudokuGroup(4,  coords[0][6], coords[1][6]),
                new KillerSudokuGroup(16,  coords[0][7], coords[1][7]),
                new KillerSudokuGroup(15,  coords[0][8], coords[1][8], coords[2][8], coords[3][8]),

                new KillerSudokuGroup(25,  coords[1][0], coords[1][1], coords[2][0], coords[2][1]),
                new KillerSudokuGroup(17,  coords[1][2], coords[1][3]),

                new KillerSudokuGroup(9,  coords[2][2], coords[2][3], coords[3][3]),
                new KillerSudokuGroup(8,  coords[2][5], coords[3][5], coords[4][5]),
                new KillerSudokuGroup(20,  coords[2][6], coords[2][7], coords[3][6]),

                new KillerSudokuGroup(6,  coords[3][0], coords[4][0]),
                new KillerSudokuGroup(14,  coords[3][1], coords[3][2]),
                new KillerSudokuGroup(17,  coords[3][4], coords[4][4], coords[5][4]),
                new KillerSudokuGroup(17,  coords[3][7], coords[4][6], coords[4][7]),

                new KillerSudokuGroup(13,  coords[4][1], coords[4][2], coords[5][1]),
                new KillerSudokuGroup(20,  coords[4][3], coords[5][3], coords[6][3]),
                new KillerSudokuGroup(12,  coords[4][8], coords[5][8]),

                new KillerSudokuGroup(27,  coords[5][0], coords[6][0], coords[7][0], coords[8][0]),
                new KillerSudokuGroup(6,  coords[5][2], coords[6][1], coords[6][2]),
                new KillerSudokuGroup(20,  coords[5][5], coords[6][5], coords[6][6]),
                new KillerSudokuGroup(6,  coords[5][6], coords[5][7]),

                new KillerSudokuGroup(10,  coords[6][4], coords[7][3], coords[7][4], coords[8][3]),
                new KillerSudokuGroup(14,  coords[6][7], coords[6][8], coords[7][7], coords[7][8]),

                new KillerSudokuGroup(8,  coords[7][1], coords[8][1]),
                new KillerSudokuGroup(16,  coords[7][2], coords[8][2]),
                new KillerSudokuGroup(15,  coords[7][5], coords[7][6]),

                new KillerSudokuGroup(13,  coords[8][4], coords[8][5], coords[8][6]),
                new KillerSudokuGroup(18,  coords[8][7], coords[8][8]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertInvalid(instance);
    }

    @Test(timeout = 1500)
    @Grade(cpuTimeout = 500)
    public void test3() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {2, 0, 6,   0, 0, 0,   0, 0, 8},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 0, 0,   0, 0, 5},
                {0, 9, 0,   4, 0, 8,   0, 0, 1},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 0, 9,   0, 0, 0},
                {0, 4, 0,   0, 5, 0,   0, 0, 9},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(15,  coords[0][0], coords[0][1], coords[0][2]),
                new KillerSudokuGroup(19,  coords[0][3], coords[0][4], coords[1][3], coords[1][4]),
                new KillerSudokuGroup(11,  coords[0][5], coords[1][5], coords[2][5]),
                new KillerSudokuGroup(18,  coords[0][6], coords[0][7], coords[1][6], coords[1][7]),
                new KillerSudokuGroup(20,  coords[0][8], coords[1][8], coords[2][8], coords[3][8]),

                new KillerSudokuGroup(21,  coords[1][0], coords[2][0], coords[3][0]),
                new KillerSudokuGroup(20,  coords[1][1], coords[2][1], coords[3][1], coords[3][2]),
                new KillerSudokuGroup(7,  coords[1][2], coords[2][2]),

                new KillerSudokuGroup(13,  coords[2][3], coords[3][3], coords[4][3]),
                new KillerSudokuGroup(16,  coords[2][4], coords[3][4], coords[3][5]),
                new KillerSudokuGroup(11,  coords[2][6], coords[3][6], coords[4][6]),
                new KillerSudokuGroup(17,  coords[2][7], coords[3][7], coords[4][7]),

                new KillerSudokuGroup(23,  coords[4][0], coords[4][1], coords[5][0], coords[5][1], coords[6][0], coords[6][1]),
                new KillerSudokuGroup(14,  coords[4][2], coords[5][2], coords[6][2]),
                new KillerSudokuGroup(26,  coords[4][4], coords[4][5], coords[5][5], coords[6][5]),
                new KillerSudokuGroup(23,  coords[4][8], coords[5][8], coords[6][8], coords[7][8]),

                new KillerSudokuGroup(29,  coords[5][3], coords[5][4], coords[6][3], coords[6][4]),
                new KillerSudokuGroup(23,  coords[5][6], coords[5][7], coords[6][6], coords[7][6]),

                new KillerSudokuGroup(11,  coords[6][7], coords[7][7]),

                new KillerSudokuGroup(23,  coords[7][0], coords[7][1], coords[8][0], coords[8][1]),
                new KillerSudokuGroup(18,  coords[7][2], coords[7][3], coords[8][2], coords[8][3]),
                new KillerSudokuGroup(12,  coords[7][4], coords[7][5], coords[8][4]),

                new KillerSudokuGroup(11,  coords[8][5], coords[8][6], coords[8][7], coords[8][8]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertInvalid(instance);
    }

    @Test(timeout = 6000)
    @Grade(cpuTimeout = 2000)
    public void test4() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0, 0, 0,   0, 0, 0,   0, 0, 8},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 0, 0,   0, 0, 5},
                {0, 9, 0,   4, 0, 8,   0, 0, 1},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 0, 9,   0, 0, 0},
                {0, 4, 0,   0, 5, 0,   0, 0, 9},
                {0, 0, 0,   0, 0, 0,   0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(15,  coords[0][0], coords[0][1], coords[0][2]),
                new KillerSudokuGroup(19,  coords[0][3], coords[0][4], coords[1][3], coords[1][4]),
                new KillerSudokuGroup(11,  coords[0][5], coords[1][5], coords[2][5]),
                new KillerSudokuGroup(22,  coords[0][6], coords[0][7], coords[1][6], coords[1][7]),
                new KillerSudokuGroup(20,  coords[0][8], coords[1][8], coords[2][8], coords[3][8]),

                new KillerSudokuGroup(21,  coords[1][0], coords[2][0], coords[3][0]),
                new KillerSudokuGroup(20,  coords[1][1], coords[2][1], coords[3][1], coords[3][2]),
                new KillerSudokuGroup(7,  coords[1][2], coords[2][2]),

                new KillerSudokuGroup(13,  coords[2][3], coords[3][3], coords[4][3]),
                new KillerSudokuGroup(16,  coords[2][4], coords[3][4], coords[3][5]),
                new KillerSudokuGroup(11,  coords[2][6], coords[3][6], coords[4][6]),
                new KillerSudokuGroup(17,  coords[2][7], coords[3][7], coords[4][7]),

                new KillerSudokuGroup(23,  coords[4][0], coords[4][1], coords[5][0], coords[5][1], coords[6][0], coords[6][1]),
                new KillerSudokuGroup(14,  coords[4][2], coords[5][2], coords[6][2]),
                new KillerSudokuGroup(26,  coords[4][4], coords[4][5], coords[5][5], coords[6][5]),
                new KillerSudokuGroup(23,  coords[4][8], coords[5][8], coords[6][8], coords[7][8]),

                new KillerSudokuGroup(29,  coords[5][3], coords[5][4], coords[6][3], coords[6][4]),
                new KillerSudokuGroup(21,  coords[5][6], coords[5][7], coords[6][6], coords[7][6]),

                new KillerSudokuGroup(11,  coords[6][7], coords[7][7]),

                new KillerSudokuGroup(25,  coords[7][0], coords[7][1], coords[8][0], coords[8][1]),
                new KillerSudokuGroup(18,  coords[7][2], coords[7][3], coords[8][2], coords[8][3]),
                new KillerSudokuGroup(12,  coords[7][4], coords[7][5], coords[8][4]),

                new KillerSudokuGroup(11,  coords[8][5], coords[8][6], coords[8][7], coords[8][8]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertValid(4, instance);
    }

    @Test(timeout = 600)
    @Grade(cpuTimeout = 200)
    public void test5() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0, 2, 0,   0, 0, 0,   0, 0, 0},
                {0, 0, 0,   6, 0, 0,   0, 0, 3},
                {0, 7, 4,   0, 8, 0,   0, 0, 0},

                {0, 0, 0,   0, 0, 3,   0, 0, 2},
                {0, 8, 0,   0, 4, 0,   0, 1, 0},
                {6, 0, 0,   5, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 1, 0,   7, 8, 0},
                {5, 4, 0,   0, 0, 9,   0, 0, 0},
                {0, 0, 0,   0, 0, 0,   0, 4, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(15,  coords[0][0], coords[0][1], coords[0][2]),
                new KillerSudokuGroup(19,  coords[0][3], coords[0][4], coords[1][3], coords[1][4]),
                new KillerSudokuGroup(11,  coords[0][5], coords[1][5], coords[2][5]),
                new KillerSudokuGroup(22,  coords[0][6], coords[0][7], coords[1][6], coords[1][7]),
                new KillerSudokuGroup(20,  coords[0][8], coords[1][8], coords[2][8], coords[3][8]),

                new KillerSudokuGroup(21,  coords[1][0], coords[2][0], coords[3][0]),
                new KillerSudokuGroup(20,  coords[1][1], coords[2][1], coords[3][1], coords[3][2]),
                new KillerSudokuGroup(7,  coords[1][2], coords[2][2]),

                new KillerSudokuGroup(13,  coords[2][3], coords[3][3], coords[4][3]),
                new KillerSudokuGroup(16,  coords[2][4], coords[3][4], coords[3][5]),
                new KillerSudokuGroup(11,  coords[2][6], coords[3][6], coords[4][6]),
                new KillerSudokuGroup(17,  coords[2][7], coords[3][7], coords[4][7]),

                new KillerSudokuGroup(23,  coords[4][0], coords[4][1], coords[5][0], coords[5][1], coords[6][0], coords[6][1]),
                new KillerSudokuGroup(14,  coords[4][2], coords[5][2], coords[6][2]),
                new KillerSudokuGroup(26,  coords[4][4], coords[4][5], coords[5][5], coords[6][5]),
                new KillerSudokuGroup(23,  coords[4][8], coords[5][8], coords[6][8], coords[7][8]),

                new KillerSudokuGroup(29,  coords[5][3], coords[5][4], coords[6][3], coords[6][4]),
                new KillerSudokuGroup(21,  coords[5][6], coords[5][7], coords[6][6], coords[7][6]),

                new KillerSudokuGroup(11,  coords[6][7], coords[7][7]),

                new KillerSudokuGroup(25,  coords[7][0], coords[7][1], coords[8][0], coords[8][1]),
                new KillerSudokuGroup(18,  coords[7][2], coords[7][3], coords[8][2], coords[8][3]),
                new KillerSudokuGroup(12,  coords[7][4], coords[7][5], coords[8][4]),

                new KillerSudokuGroup(11,  coords[8][5], coords[8][6], coords[8][7], coords[8][8]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertInvalid(instance);
    }

    @Test(timeout = 600)
    @Grade(cpuTimeout = 200)
    public void test6() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0, 2, 0,   0, 0, 0,   0, 0, 0},
                {0, 0, 0,   6, 0, 0,   0, 0, 3},
                {0, 7, 4,   0, 8, 0,   1, 2, 0},

                {0, 0, 0,   0, 0, 3,   8, 0, 2},
                {0, 8, 0,   0, 4, 0,   0, 1, 0},
                {6, 0, 0,   5, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 1, 0,   7, 8, 0},
                {5, 4, 0,   0, 0, 9,   0, 0, 0},
                {0, 0, 0,   8, 0, 0,   0, 4, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[9];
        for (int i = 0 ; i < 9 ; ++i) {
            groups[i] = new KillerSudokuGroup(45, coords[i]);
        }
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertValid(1, instance);
    }

    @Test(timeout = 4500)
    @Grade(cpuTimeout = 1500)
    public void test7() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {2, 0, 0,   0, 0, 0,   0, 0, 0},
                {8, 0, 4,   0, 6, 2,   7, 0, 0},
                {0, 0, 3,   8, 0, 0,   0, 0, 0},

                {0, 0, 0,   0, 2, 0,   0, 9, 0},
                {5, 0, 0,   0, 0, 0,   6, 2, 1},
                {0, 3, 2,   0, 0, 6,   0, 0, 0},

                {0, 2, 0,   7, 0, 9,   1, 4, 0},
                {6, 0, 1,   0, 5, 3,   8, 0, 9},
                {0, 0, 0,   0, 0, 1,   0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[9];
        for (int i = 0 ; i < 9 ; ++i) {
            groups[i] = new KillerSudokuGroup(45, coords[i]);
        }
        KillerSudokuInstance instance = new KillerSudokuInstance(9, groups);
        assertValid(163, instance);
    }

}
