package astar.solver;

import util.Solution;
import java.util.function.BiConsumer;


public class WeightedAstar<S extends State> implements Solver {
    Model<S> model; // the model to solve
    private final double hWeight; // the weight accorded to the heuristic in the f score

    /**
     * Creates a WeightedAstar object to solve the given problem
     * @param model the model to solve
     * @param hWeight the heuristic weight accorded to the heuristic in the f score
     */
    public WeightedAstar(Model<S> model, double hWeight) {
        this.model = model;
        this.hWeight = hWeight;
    }

    /**
     * Search for a solution for the given problem.
     * Returns the first solution found.
     * @return the solution containing the objective value and the decisions taken
     */
    @Override
    public void solve(BiConsumer<Solution, SearchStatistics> onSolution) {
        // TODO

        throw new IllegalArgumentException("No solution found");
    }

    /**
     * Rebuild and returns the optimal solution of the problem and the set of decisions taken to obtain it
     * @return the optimal solution of the problem and the set of decisions taken to obtain it
     */
    private Solution rebuildSolution(S state, double value) {
        // TODO
         return null;
    }
}
