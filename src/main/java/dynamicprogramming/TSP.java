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

        // initialize root state
    }

    @Override
    boolean isBaseCase(TSPState state) {
         return false;
    }

    @Override
    double getBaseCaseValue(TSPState state) {
         return 0;
    }

    @Override
    TSPState getRootState() {
        return root;
    }

    @Override
    List<Transition<TSPState>> getTransitions(TSPState state) {
         return null;
    }

    @Override
    boolean isMaximization() {
        return false;
    }
    
}
