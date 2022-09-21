package dynamicprogramming;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import util.tsp.TSPInstance;

public class TSP extends Model<TSPState> {

    TSPInstance instance;
    TSPState root;

    public TSP(TSPInstance instance) {
        this.instance = instance;

        // TODO initialize root state, assuming the salesman starts at city 0
    }

    @Override
    boolean isBaseCase(TSPState state) {
        // TODO checks if the state is a base case
         return false;
    }

    @Override
    double getBaseCaseValue(TSPState state) {
        // TODO return the value of the base case, don't forget the salesman must go back to city 0
         return 0;
    }

    @Override
    TSPState getRootState() {
        return root;
    }

    @Override
    List<Transition<TSPState>> getTransitions(TSPState state) {
        // TODO specify the transitions applicable to the given state
         return null;
    }

    @Override
    boolean isMaximization() {
        return false;
    }
    
}
