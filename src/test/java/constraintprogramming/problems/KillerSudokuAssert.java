package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class KillerSudokuAssert {

    public static void assertSolution(KillerSudokuInstance instance, KillerSudokuInstance.Solution solution) {
        assertEquals(instance.n(), solution.n());
        int n = instance.n();
        boolean[] seen = new boolean[n+1];
        for (int i = 0 ; i < n ; ++i) {
            // all rows have different numbers
            for (int j = 0 ; j < n ; ++j) {
                int v = solution.value(i, j);
                assertTrue("Each number should be >= 1", v >= 1);
                assertTrue("Each number should be <= n for a n*n killer sudoku", v <= n);
                assertFalse("The value " + v + "appears twice on the row", seen[v]);
                if (instance.isValue(i, j)) {
                    assertEquals("The value at (" + i + "," + j + ") does not match with the number given at input",
                            instance.value(i, j), v);
                }
                seen[v] = true;
            }
            for (int j = 1 ; j < seen.length ; ++j) {
                assertTrue("The number " + j + "does not appear in solution", seen[j]);
            }
            Arrays.fill(seen, false);
            // all columns have different numbers
            for (int j = 0 ; j < n ; ++j) {
                int v = solution.value(j, i);
                assertTrue("Each number should be >= 1", v >= 1);
                assertTrue("Each number should be <= n for a n*n killer sudoku", v <= n);
                assertFalse("The value " + v + "appears twice on the column", seen[v]);
                seen[v] = true;
            }        
            for (int j = 1 ; j < seen.length ; ++j) {
                assertTrue("The number " + j + "does not appear in solution", seen[j]);
            }
            Arrays.fill(seen, false);
        }
        // all sub-squares have different numbers
        int subSquare = (int) Math.sqrt(n);
        for (int iOffset = 0 ; iOffset < n ; iOffset += subSquare) {
            for (int jOffset = 0 ; jOffset < n ; jOffset += subSquare) {
                for (int i = iOffset ; i < iOffset + subSquare ; ++i) {
                    for (int j = jOffset ; j < jOffset + subSquare ; ++j) {
                        int v = solution.value(i, j);
                        seen[v] = true;
                    }
                }
                for (int j = 1 ; j < seen.length ; ++j) {
                    assertTrue("The number " + j + "does not appear in solution", seen[j]);
                }
                Arrays.fill(seen, false);
            }
        }
        // all groups sum to their associated value
        for (KillerSudokuGroup group : instance.groups()) {
            int sum = 0;
            for (Coordinate c: group.values()) {
                sum += solution.value(c.i(), c.j());
            }
            assertEquals("The cells within a group do not sum up to the expected value", group.sum(), sum);
        }
    }
    
    public static void assertValid(int nSol, KillerSudokuInstance instance) {
        try {
            KillerSudokuSolver solver = new KillerSudokuSolver(instance);
            List<KillerSudokuInstance.Solution> solutionList = filter(solver.solve());
            assertEquals("You did not find all solutions to the sudoku killer", nSol, solutionList.size());
            for (KillerSudokuInstance.Solution s: solutionList)
                assertSolution(instance, s);
        } catch (TinyCSP.Inconsistency e) {
            fail("The sudoku killer given as input is valid but you thrown an inconsistency");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }
    
    public static void assertInvalid(KillerSudokuInstance instance) {
        try {
            KillerSudokuSolver solver = new KillerSudokuSolver(instance);
            List<KillerSudokuInstance.Solution> solutionList = solver.solve();
            assertEquals("There is no solution to this problem", 0, solutionList.size());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            // throwing an inconsistency could also be valid
        }
    }

    private static List<KillerSudokuInstance.Solution> filter(List<KillerSudokuInstance.Solution> solutionList) {
        HashSet<Integer> unique = new HashSet<>();
        List<KillerSudokuInstance.Solution> filtered = new ArrayList<>();
        for (KillerSudokuInstance.Solution s: solutionList) {
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
