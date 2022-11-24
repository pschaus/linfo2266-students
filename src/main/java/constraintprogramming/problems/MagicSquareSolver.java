package constraintprogramming.problems;

import constraintprogramming.solver.TinyCSP;
import constraintprogramming.solver.Variable;
import util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves a magic square problem
 * A magic square is a square of size n*n, with the following constraints:
 * - each number within a cell is included between 1 (included) and n*n (included)
 * - each number can appear once and only once within the magic square
 * - the sum of each row, columns and of the 2 diagonals are equal
 */
public class MagicSquareSolver {

    public final MagicSquareInstance instance;

    public MagicSquareSolver(MagicSquareInstance instance) {
        this.instance = instance;
    }

    /**
     * Solves the instance {@link MagicSquareSolver#instance} by
     * - creating the variables {@link TinyCSP#makeVariable(int)} of the problems
     * - adding the constraints of the problem to the variables
     *   (see the methods within {@link TinyCSP} for a list of the constraints)
     * and returns all {@link constraintprogramming.problems.MagicSquareInstance.Solution} related to it
     *
     * @return list of solutions to the given instance (possibly empty if no solution exists)
     */
    public List<MagicSquareInstance.Solution> solve() {
        TinyCSP csp = new TinyCSP();
        List<MagicSquareInstance.Solution> listSol = new ArrayList<>();
        int n = instance.n();
        // TODO 1 create the variables for your problem
        //  don't forget to take into account the possibly already (un)set values in the magic square!
        //  you can check if a value is set with the instance.isValue() method
        // TODO 2 add constraints on your variables

        csp.dfs(() -> {
            // TODO 3 set the values of solution based on your fixed variables
            int[][] solution = new int[n][n];
            MagicSquareInstance.Solution sol = new MagicSquareInstance.Solution(solution);
            //System.out.println(sol+"\n");
            listSol.add(sol);
        });
        return listSol;
    }

    /**
     * Computes the magic constant of the magic square
     * The magic constant is the value of the sum that needs to be found
     *
     * @return expected sum across all rows, columns and diagonals
     */
    public int getMagicConstant() {
        // TODO 4 (not needed for correctness but useful for efficiency) compute the sum of a row within a n*n magic square
        //  and use it to enhance your model
        throw new NotImplementedException("getMagicConstant");
    }

    public static void main(String[] args) {
        int[][] values = new int[][] {
                {6, 0, 0},
                {1, 0, 0},
                {0, 0, 0},
        };
        MagicSquareInstance instance = new MagicSquareInstance(values);
        System.out.println(instance);
        MagicSquareSolver solver = new MagicSquareSolver(instance);
        List<MagicSquareInstance.Solution> solutionList = solver.solve();
        System.out.println("# solutions = " + solutionList.size());
        System.out.println("1st solution = \n" + solutionList.get(0));
    }

}
