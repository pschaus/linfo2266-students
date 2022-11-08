package localsearch;

/**
 * This class represents an invariant - or function - that is computed
 * from a set of variables. The value of this invariant needs to reflect
 * the updates made to the variables it concerns.
 */
public abstract class Invariant {

    /**
     * Creates an invariant taking care of the given variables
     * and registers itself on all variables to be notified of updates
     * @param variables the variables related to the invariant
     */
    public Invariant(IntVar[] variables) {
        for (IntVar variable : variables) {
            variable.register(this);
        }
    }
    
    /**
     * Function called when variable x is assigned a new value
     * The invariant should update its value to reflect the changes
     * made to variable x
     * @param x the variable that has been updated
     * @param oldValue the previous value assigned to x
     */
    abstract void update(IntVar x, int oldValue);

    /**
     * Returns the value of the function computed by the invariant
     * @return the value of the function computed by the invariant
     */
    abstract int getValue();

}
