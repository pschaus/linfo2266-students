package localsearch;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents a variable of a problem with a fixed ID
 * and a value that can be modified throughout the search.
 * Invariants can be attached to the variable and notified on updates.
 */
public class IntVar {

    private final int id;
    private int value;
    private List<Invariant> invariants;
    
    public IntVar(int id, int value) {
        this.id = id;
        this.value = value;
        this.invariants = new LinkedList<>();
    }

    /**
     * IntVar constructor for when the variable has no ID
     * @param value the initial value assigned to the variable
     */
    public IntVar(int value) {
        this(-1, value);
    }

    /**
     * Registers an invariant that will be notified when
     * the variable is updated
     * @param invariant the invariant
     */
    public void register(Invariant invariant) {
        this.invariants.add(invariant);
    }

    /**
     * Returns the ID of the variable
     * @return the ID of the variable
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the value currently assigned to the variable
     * @return the value currently assigned to the variable
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Assign a new value to the variable
     * Notifies all registered invariants
     * @param value the nex value
     */
    public void setValue(int value) {
        int oldValue = this.value;
        this.value = value;
        for (Invariant invariant : this.invariants) {
            invariant.update(this, oldValue);
        }
    }

    /**
     * Swaps the values of the two variables
     * @param other the other variable to swap
     */
    public void swap(IntVar other) {
        int tmp = this.getValue();
        this.setValue(other.getValue());
        other.setValue(tmp);
    }

    /**
     * Returns an array of n new variables with the given initial value
     * @param n the number of variables in the array
     * @param value the initial value assigned to all variables
     * @return the array of variables
     */
    public static IntVar[] makeIntVarArray(int n, int value) {
        IntVar[] vars = new IntVar[n];
        for (int i = 0; i < n; i++) {
            vars[i] = new IntVar(i, value);
        }
        return vars;
    }

}
