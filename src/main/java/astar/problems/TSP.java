package astar.problems;

import astar.solver.Model;
import astar.solver.Transition;
import util.tsp.TSPInstance;
import java.util.List;


public class TSP extends Model<TSPState> {

    TSPInstance instance;
    TSPState root;

    public TSP(TSPInstance instance) {
        this.instance = instance;
        // TODO initialize root state, assuming the salesman starts at city 0
    }

    @Override
    public boolean isTerminalState(TSPState state) {
        // TODO checks if the state is a terminal state
         return false;
    }

    @Override
    public double getTerminalStateValue(TSPState state) {
        // TODO return the value of the terminal state, don't forget the salesman must go back to city 0
         return 0;
    }

    @Override
    public TSPState getRootState() {
        return root;
    }

    @Override
    public List<Transition<TSPState>> getTransitions(TSPState state) {
        // TODO specify the transitions applicable to the given state
         return null;
    }


    @Override
    public double h(TSPState state) {
        // TODO return the heuristic value for the given state
         return Double.MAX_VALUE;
    }

}
