package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class MagicSquareAssert {

    public static void assertSolution(MagicSquareInstance instance, MagicSquareInstance.Solution solution) {
        assertEquals(instance.n(), solution.n());
        int n = instance.n();
        boolean[] seen = new boolean[n*n+1]; // seen[v] set to true if v appears in the solution
        Integer sum = null;
        int[] colSum = new int[n];
        for (int i = 0 ; i < n ; ++i) {
            int rowSum = 0;
            for (int j = 0 ; j < n ; ++j) {
                int v = solution.value(i, j);
                assertTrue("Each number should be >= 1", v >= 1);
                assertTrue("Each number should be <= n*n for a n*n magic square", v <= n*n);
                assertFalse("The number " +  v + " appears twice", seen[v]);
                if (instance.isValue(i, j)) {
                    assertEquals("The initial value within at coordinate (" + i + "," + j +") " +
                            "is not the same between the input and the solution",
                            instance.value(i, j), v);
                }
                seen[v] = true;
                rowSum += v;
                colSum[j] += v;
            }
            if (sum == null) {
                sum = rowSum;
            } else {
                assertEquals("The sum is not always the same across all rows", sum.intValue(), rowSum);
            }
        }
        for (int s : colSum) {
            assertEquals("The sum is not always the same between the columns and the rows", sum.intValue(), s);
        }
        int mainDiagSum = 0;
        int secondDiagSum = 0;
        for (int i = 0 ; i < n ; ++i) {
            mainDiagSum += solution.value(i, i);
            secondDiagSum += solution.value(n-1-i, i);
        }
        assert sum != null;
        assertEquals("The sum is not the same between the main diagonal and the rows", sum.intValue(), mainDiagSum);
        assertEquals("The sum is not the same between the second diagonal and the rows", sum.intValue(), secondDiagSum);
        for (int i = 1 ; i < seen.length ; ++i) {
            assertTrue("The number " + i + "does not appear in solution", seen[i]);
        }
        Arrays.hashCode(new int[5]);
    }

    public static void assertValid(int[][] values, int nSol) {
        try {
            MagicSquareInstance instance = new MagicSquareInstance(values);
            MagicSquareSolver solver = new MagicSquareSolver(instance);
            List<MagicSquareInstance.Solution> solutionList = filter(solver.solve());
            assertEquals("You did not find all solutions to the magic square", nSol, solutionList.size());
            for (MagicSquareInstance.Solution s: solutionList)
                assertSolution(instance, s);
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("The magic square given as input is valid but you thrown an inconsistency");
        }
    }

    public static void assertInvalid(int[][] values) {
        try {
            MagicSquareInstance instance = new MagicSquareInstance(values);
            MagicSquareSolver solver = new MagicSquareSolver(instance);
            List<MagicSquareInstance.Solution> solutionList = solver.solve();
            assertEquals("There is no solution to this problem", 0, solutionList.size());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            // throwing an inconsistency could also be valid
        }
    }

    private static List<MagicSquareInstance.Solution> filter(List<MagicSquareInstance.Solution> solutionList) {
        HashSet<Integer> unique = new HashSet<>();
        List<MagicSquareInstance.Solution> filtered = new ArrayList<>();
        for (MagicSquareInstance.Solution s: solutionList) {
            Integer hashcode = hashCode(s.values);
            if (!unique.contains(hashcode)) {
                unique.add(hashcode);
                filtered.add(s);
            }
        }
        return filtered;
    }

    public static int hashCode(int[][] a) {
        if (a == null) {
            return 0;
        } else {
            int result = 1;
            for (int[] b: a) {
                result = 29 * result + Arrays.hashCode(b);
            }
            return result;
        }
    }

}
