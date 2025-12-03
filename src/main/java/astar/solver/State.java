package astar.solver;

/**
 * State interface for A* like model
 * Equivalent states should have equal hash values
 */
public abstract class State {

    /**
     * Computes a hash value that uniquely identifies a state of the A* model
     * Equivalent states should thus have equal hash values
     * Hint: use Objects.hash(...) with the fields related to the A*state
     * @return a hash of the state
     */
    public abstract int hash();

    /**
     * Returns true if both states are equal, needed in case of collisions in the hash table
     * @return true if both states are equal
     */
    public abstract boolean isEqual(State state);

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
