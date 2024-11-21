package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTourAssert {

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

    public static void assertSolution(KnightTourInstance instance, KnightTourInstance.Solution solution) {
        int n = instance.n();
        boolean[] seen = new boolean[n*n];
        for (int i = 0; i < n*n; i++) {
            int v = solution.move(i);
            assertTrue(v >= 0, "Each value should be >= 0");
            assertTrue(v < n*n, "Each value should be < n*n");
            assertFalse(seen[v], "The value " + v + "appears twice on the row");
            if (instance.isMove(i)) {
                assertEquals(instance.move(i), v,
                        "The initial move at step " + i +
                                "is not the same between the input and the solution");
            }

            seen[v] = true;
        }

        for (int i = 0; i < n*n; i++) {
            int current = solution.move(i);
            int next = solution.move((i+1)%(n*n));
            Set<Integer> accessible = accessible(current, n);
            assertTrue(accessible.contains(next), "The move from " + current + " to " + next + " is not a knight move");
        }
    }

    public static void assertValid(KnightTourInstance instance, int nSol) {
        try {
            List<KnightTourInstance.Solution> solutionList = KnightTourSolver.solve(instance);
            assertEquals(nSol, solutionList.size(), "You did not find all solutions to the euler tour");
            for (KnightTourInstance.Solution solution : solutionList) {
                assertSolution(instance, solution);
            }
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("The euler tour given as input is valid but you thrown an inconsistency");
        }
    }

    public static void assertInvalid(KnightTourInstance instance) {
        try {
            List<KnightTourInstance.Solution> solutionList = KnightTourSolver.solve(instance);
            assertEquals(0, solutionList.size(), "There is no solution to this problem");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            // throwing an inconsistency could also be valid
        }
    }
}
