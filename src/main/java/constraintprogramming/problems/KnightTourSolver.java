package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import constraintprogramming.solver.Variable;
import constraintprogramming.solver.KnightMove;

import java.util.ArrayList;
import java.util.List;


/**
 * A knight's tour is a sequence of moves of a knight on a chessboard
 * such that the knight visits every square exactly once
 * and returns to the starting square.
 * https://en.wikipedia.org/wiki/Knight%27s_tour
 *
 * An instance of the problem can force the knight
 * to be at specific positions at specific timesteps.
 *
 * You have to model the problem as a CSP and solve it.
 * The model uses an array of n*n variables x, where
 * x[i] is the position of the knight at timestep i.
 * Every visited position should be different.
 * The knight can only move to a position that is accessible from the current position.
 * The circuit must thus be Eulerian and closed,
 * that is the knight must return to the starting position.
 */
public class KnightTourSolver {

    public static List<KnightTourInstance.Solution> solve(KnightTourInstance instance) {
        TinyCSP csp = new TinyCSP();
        int n = instance.n();

        // Variables
        Variable[] x = new Variable[n*n];
        for (int i = 0; i < n*n; i++) {
            x[i] = csp.makeVariable(n*n);
        }

        // Constraints
        // TODO add the constraints to the CSP

        // collect all the solutions
        ArrayList<KnightTourInstance.Solution> solutions = new ArrayList<>();
        csp.dfs(() -> {
            int[] moves = new int[n*n];
            for (int i = 0; i < n*n; i++) {
                moves[i] = x[i].dom.value();
            }
            KnightTourInstance.Solution sol = new KnightTourInstance.Solution(moves);
            solutions.add(sol);
        });

        return solutions;
    }

}
