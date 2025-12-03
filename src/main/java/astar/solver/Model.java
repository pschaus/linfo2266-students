package astar.solver;

import java.util.List;

/**
 * Interface for describing an A* based model
 */
public abstract class Model<S extends State> {

    /**
     * @return true if the state is a terminal state of the A* model
     */
    public abstract boolean isTerminalState(S state);

    /**
     * @return the value of the terminal state
     */
    public abstract double getTerminalStateValue(S state);

    /**
     * @return the root state of the A* model
     */
    public abstract S getRootState();

    /**
     * @return the list of transitions from the given state
     */
    public abstract List<Transition<S>> getTransitions(S state);


    public abstract double h(S state);
}