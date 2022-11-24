package constraintprogramming.problems;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;

import static constraintprogramming.problems.KillerSudokuAssert.assertInvalid;
import static constraintprogramming.problems.KillerSudokuAssert.assertValid;

@GradeClass(totalValue = 20)
public class KillerSudokuTestFast {

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void test2x2WithInitialValues() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {1, 0, 3, 0},
                {0, 0, 0, 4},
                {0, 0, 0, 0},
                {2, 0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(8,  coords[0][0], coords[0][1], coords[0][2]),
                new KillerSudokuGroup(12, coords[0][3], coords[1][2], coords[1][3], coords[2][2], coords[2][3]),
                new KillerSudokuGroup(9,  coords[2][1], coords[3][1], coords[3][2], coords[3][3]),
                new KillerSudokuGroup(11, coords[1][0], coords[1][1], coords[2][0], coords[3][0]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(4, groups);
        assertValid(1, instance);
    }

    @Test(timeout = 1200)
    @Grade(cpuTimeout = 400)
    public void test2x2WithoutInitialValues() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(8,  coords[0][0], coords[0][1], coords[0][2]),
                new KillerSudokuGroup(12, coords[0][3], coords[1][2], coords[1][3], coords[2][2], coords[2][3]),
                new KillerSudokuGroup(9,  coords[2][1], coords[3][1], coords[3][2], coords[3][3]),
                new KillerSudokuGroup(11, coords[1][0], coords[1][1], coords[2][0], coords[3][0]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(4, groups);
        assertValid(8, instance);
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void test1x1() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(1,  coords[0][0]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(1, groups);
        assertValid(1, instance);
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testIncoherent1() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(2,  coords[0][0]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(1, groups);
        assertInvalid(instance);
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testIncoherent2() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {2},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(2,  coords[0][0]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(1, groups);
        assertInvalid(instance);
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testIncoherent3() {
        Coordinate[][] coords = Coordinate.fromMatrix(new int[][]{
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
        });
        KillerSudokuGroup[] groups = new KillerSudokuGroup[] {
                new KillerSudokuGroup(8,  coords[0][0], coords[0][1], coords[0][2]),
                new KillerSudokuGroup(12, coords[0][3], coords[1][2], coords[1][3], coords[2][2], coords[2][3]),
                new KillerSudokuGroup(9,  coords[2][1], coords[3][1], coords[3][2], coords[3][3]),
                new KillerSudokuGroup(11, coords[1][0], coords[1][1], coords[2][0], coords[3][0]),
        };
        KillerSudokuInstance instance = new KillerSudokuInstance(4, groups);
        assertInvalid(instance);
    }

}
