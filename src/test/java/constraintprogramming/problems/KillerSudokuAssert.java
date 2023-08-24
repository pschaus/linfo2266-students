package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class KillerSudokuAssert {

    public static void assertSolution(KillerSudokuInstance instance, KillerSudokuInstance.Solution solution) {
        assertEquals(instance.n(), solution.n());
        int n = instance.n();
        boolean[] seen = new boolean[n+1];
        for (int i = 0 ; i < n ; ++i) {
            // all rows have different numbers
            for (int j = 0 ; j < n ; ++j) {
                int v = solution.value(i, j);
                assertTrue(v >= 1, "Each number should be >= 1");
                assertTrue(v <= n, "Each number should be <= n for a n*n killer sudoku");
                assertFalse(seen[v], "The value " + v + "appears twice on the row");
                if (instance.isValue(i, j)) {
                    assertEquals(instance.value(i, j), v,
                            "The value at (" + i + "," + j + ") does not match with the number given at input");
                }
                seen[v] = true;
            }
            for (int j = 1 ; j < seen.length ; ++j) {
                assertTrue(seen[j], "The number " + j + "does not appear in solution");
            }
            Arrays.fill(seen, false);
            // all columns have different numbers
            for (int j = 0 ; j < n ; ++j) {
                int v = solution.value(j, i);
                assertTrue(v >= 1, "Each number should be >= 1");
                assertTrue(v <= n, "Each number should be <= n for a n*n killer sudoku");
                assertFalse(seen[v], "The value " + v + "appears twice on the column");
                seen[v] = true;
            }        
            for (int j = 1 ; j < seen.length ; ++j) {
                assertTrue(seen[j], "The number " + j + "does not appear in solution");
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
                    assertTrue(seen[j], "The number " + j + "does not appear in solution");
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
            assertEquals(group.sum(), sum, "The cells within a group do not sum up to the expected value");
        }
    }

    public static void assertValid(int nSol, KillerSudokuInstance instance) {
        try {
            KillerSudokuSolver solver = new KillerSudokuSolver(instance);
            List<KillerSudokuInstance.Solution> solutionList = filter(solver.solve());
            assertEquals(nSol, solutionList.size(), "You did not find all solutions to the sudoku killer");
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
            assertEquals(0, solutionList.size(), "There is no solution to this problem");
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
