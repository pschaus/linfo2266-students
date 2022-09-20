package dynamicprogramming;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import util.Solution;

public class DynamicProgramming<S extends State> {

    Model<S> model;               // the dynamic programming model to solve
    HashMap<State, Double> table; // table to store the best value found for each state

    public DynamicProgramming(Model<S> model) {
        this.model = model;
        this.table = new HashMap<>();
    }

    public Solution getSolution() {
        // compute the solution for the root state of the model

        // rebuild and return the solution

         return null;
    }

    private double getValueForState(S state) {
        // check if the state is a base case of the model, use predefined value if so

        // check if the solution was already for the given state, use result if so

        // otherwise, solve for all successor states then store and return the best value
        // note that the problem might be a maximization or a minimization

         return 0;
    }

    private Solution rebuildSolution() {
        // rebuild solution by traversing the table

         return null;
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