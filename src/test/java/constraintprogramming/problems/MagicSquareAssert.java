package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
                assertTrue(v >= 1, "Each number should be >= 1");
                assertTrue(v <= n*n, "Each number should be <= n*n for a n*n magic square");
                assertFalse(seen[v], "The number " +  v + " appears twice");
                if (instance.isValue(i, j)) {
                    assertEquals(instance.value(i, j), v,
                            "The initial value within at coordinate (" + i + "," + j +") " +
                            "is not the same between the input and the solution");
                }
                seen[v] = true;
                rowSum += v;
                colSum[j] += v;
            }
            if (sum == null) {
                sum = rowSum;
            } else {
                assertEquals(sum.intValue(), rowSum, "The sum is not always the same across all rows");
            }
        }
        for (int s : colSum) {
            assertEquals(sum.intValue(), s, "The sum is not always the same between the columns and the rows");
        }
        int mainDiagSum = 0;
        int secondDiagSum = 0;
        for (int i = 0 ; i < n ; ++i) {
            mainDiagSum += solution.value(i, i);
            secondDiagSum += solution.value(n-1-i, i);
        }
        assert sum != null;
        assertEquals(sum.intValue(), mainDiagSum, "The sum is not the same between the main diagonal and the rows");
        assertEquals(sum.intValue(), secondDiagSum, "The sum is not the same between the second diagonal and the rows");
        for (int i = 1 ; i < seen.length ; ++i) {
            assertTrue(seen[i], "The number " + i + "does not appear in solution");
        }
    }

    public static void assertValid(int[][] values, int nSol) {
        try {
            MagicSquareInstance instance = new MagicSquareInstance(values);
            MagicSquareSolver solver = new MagicSquareSolver(instance);
            List<MagicSquareInstance.Solution> solutionList = filter(solver.solve());
            assertEquals(nSol, solutionList.size(), "You did not find all solutions to the magic square");
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
            assertEquals(0, solutionList.size(), "There is no solution to this problem");
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
