package astar.solver;

import util.Solution;
import java.util.function.BiConsumer;



public class AnytimeWeightedAstar<S extends State> implements Solver {


    final double hWeight; // the weight accorded to the heuristic in the f score
    final Model<S> model; // the model to solve
    final int timeLimit;  // the time limit (!!! in seconds !!!) before returning the best found solution


    /**
     * Creates an AnytimeWeightedAstar object to solve the given problem
     * @param model the model to solve
     * @param hWeight the heuristic weight accorded to the heuristic in the f score
     * @param timeLimit the time limit (!!! in seconds !!!)
     */
    public AnytimeWeightedAstar(Model<S> model, double hWeight, int timeLimit) {
        this.model = model;
        this.hWeight = hWeight;
        this.timeLimit = timeLimit;
    }

    /**
     * Search for solutions for the given problem.
     * Returns the best solution found when the optimal one is found or when the time limit is reached
     * @return the solution containing the objective value and the decisions taken
     */
    @Override
    public void solve(BiConsumer<Solution, SearchStatistics> onSolution) {
        // TODO
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



