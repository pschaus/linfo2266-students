package competition;


import java.util.Set;

/**
 * Abstract class that to solve the problem.
 * Extend this class to implement a new solver.
 * For example: BranchAndBoundSolver, DynamicProgrammingSolver, ...
 * The goal is to be creative, those are just examples.
 */
abstract class Solver {
    SetCoverInstance problem;

    public Solver(SetCoverInstance problem) {
        this.problem = problem;
    }

    /**
     * Method called to solve the function
     * @return the set of indices of the selected paths
     */
    abstract Set<Integer> solve();

}
