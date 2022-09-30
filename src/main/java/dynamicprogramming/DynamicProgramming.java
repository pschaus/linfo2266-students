package dynamicprogramming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import util.Solution;

public class DynamicProgramming<S extends State> {

    Model<S> model;               // the dynamic programming model to solve
    HashMap<State, Double> table; // table to store the best value found for each state

    /**
     * Creates a DynamicProgramming object for the given problem
     * @param model the DP problem to solve
     */
    public DynamicProgramming(Model<S> model) {
        this.model = model;
        this.table = new HashMap<>();
    }

    /**
     * Computes the optimal solution of the DP problem and returns the Solution object
     * @return the solution containing the objective value and the decisions taken
     */
    public Solution getSolution() {
        // TODO compute the solution for the root state of the model

        // TODO rebuild and return the solution
        return rebuildSolution();
    }

    /**
     * Computes the optimal solution for a given state of the DP model
     * @param state a state of the DP model
     * @return the value of the optimal solution for the given state
     */
    private double getValueForState(S state) {
        // TODO compute the optimal solution for the given state

        // check if the state is a base case of the model, use predefined value if so

        // check if the solution was already for the given state, use result if so

        // otherwise, solve for all successor states then store and return the best value
        // note that the problem might be a maximization or a minimization
        if (model.isBaseCase(state)){
            table.put(state, model.getBaseCaseValue(state));
            return model.getBaseCaseValue(state);
        }
        if (table.containsKey(state)) {
            return table.get(state);
        }
        List<Transition<S>> transi = model.getTransitions(state);
        double res;
        if(model.isMaximization()){
            res = Double.MIN_VALUE;
        }
        else {
            res = Double.MAX_VALUE;
        }
        for (Transition<S> t : transi) {
            double val = this.getValueForState(t.getSuccessor());
            if(model.isMaximization()){
                res = Math.max(val+t.getValue(),res);
            }
            else {
                res = Math.min(val+t.getValue(), res);
            }
        }
        table.put(state, res);
        return res;
    }

    /**
     * Recomputes and returns the optimal solution of the DP problem and the set of decisions taken to obtain it
     * @return the optimal solution of the DP problem and the set of decisions taken to obtain it
     */
    private Solution rebuildSolution() {
        // TODO rebuild solution by traversing the table
        List<Integer> l = new ArrayList<Integer>();
        Solution sol = new Solution (getValueForState(model.getRootState()),l );
        return sol;

    }

}

/**
 * Interface for describing a dynamic programming model
 */
abstract class Model<S extends State> {

    /**
     * Returns true if the state is a base case of the dynamic programming model
     * @return true if the state is a base case of the dynamic programming model
     */
    abstract boolean isBaseCase(S state);

    /**
     * Returns the value of the base case
     * @return the value of the base case
     */
    abstract double getBaseCaseValue(S state);

    /**
     * Returns the root state of the dynamic programming model, the one that represents the whole problem
     * @return the root state of the dynamic programming model
     */
    abstract S getRootState();

    /**
     * Returns the list of transitions from the given state
     * @return the list of transitions from the given state
     */
    abstract List<Transition<S>> getTransitions(S state);

    /**
     * Returns true if the problem is a maximization
     * @return true if the problem is a maximization
     */
    abstract boolean isMaximization();
}

/**
 * State interface for dynamic programming
 * Equivalent states should have equal hash values
 */
abstract class State {

    /**
     * Computes a hash value that uniquely identifies a state of the dynamic programming model
     * Equivalent states should thus have equal hash values
     * Hint: use Objects.hash(...) with the fields related to the dynamic programming state
     * @return a hash of the state
     */
    abstract int hash();

    /**
     * Returns true if both states are equal, needed in case of collisions in the hash table
     * @return true if both states are equal
     */
    abstract boolean isEqual(State state);
    
    @Override
    public int hashCode() {
        return this.hash();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof State) {
            State state = (State) o;
            return isEqual(state);
        }
        return false;
    }

}

/**
 * Class representing a transition in the dynamic programming model
 */
class Transition<S extends State> {

    private S successor;
    private int decision;
    private double value;

    public Transition(S successor, int decision, double value) {
        this.successor = successor;
        this.decision = decision;
        this.value = value;
    }

    public S getSuccessor() {
        return successor;
    }

    public int getDecision() {
        return decision;
    }

    public double getValue() {
        return value;
    }
}
