package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import constraintprogramming.solver.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves a Killer Sudoku problem
 * A killer sudoku is a variation of the sudoku, with the following constraints:
 * - each cell {@link Coordinate} contains a number between 1 (included) and n (included)
 * - each row must contain different values
 * - each column must contain different values
 * - each sub-square must contain different values
 * - all cells within the problem belong to a given group {@link KillerSudokuInstance#groups()}.
 *     The values within each group sum to a given number {@link KillerSudokuGroup#sum()}
 */
public class KillerSudokuSolver {

    private final KillerSudokuInstance instance;

    public KillerSudokuSolver(KillerSudokuInstance instance) {
        this.instance = instance;
    }

    /**
     * Solves the instance {@link KillerSudokuSolver#instance} by
     * - creating the variables {@link TinyCSP#makeVariable(int)} of the problems
     * - adding the constraints of the problem to the variables
     *   (see the methods within {@link TinyCSP} for a list of the constraints)
     * and returns all {@link constraintprogramming.problems.KillerSudokuInstance.Solution} related to it
     *
     * @return list of solutions to the given instance (possibly empty if no solution exists)
     */
    public List<KillerSudokuInstance.Solution> solve() {
        TinyCSP csp = new TinyCSP();
        List<KillerSudokuInstance.Solution> listSol = new ArrayList<>();
        int n = instance.n();
        // TODO 1 create the variables for your problem
        //  don't forget to take into account the possibly already (un)set values in the magic square!
        //  you can check if a value is set with the instance.isValue() method
        // TODO 2 add constraints on your variables
        csp.dfs(() -> {
            int[][] solution = new int[n][n];
            // TODO 3 set the values of solution based on your fixed variables
            KillerSudokuInstance.Solution sol = new KillerSudokuInstance.Solution(solution);
            //System.out.println(sol+"\n");
            listSol.add(sol);
        });
        return listSol;
    }

    public static void main(String[] args) {
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
        System.out.println(instance+"\n");
        KillerSudokuSolver solver = new KillerSudokuSolver(instance);
        List<KillerSudokuInstance.Solution> solutions = solver.solve();
        System.out.println("# solutions = " + solutions.size());
        System.out.println("1st solution = \n" + solutions.get(0));
    }

}
