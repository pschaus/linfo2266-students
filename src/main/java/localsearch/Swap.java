package localsearch;

import java.util.HashSet;

/**
 * This class represents swap moves with any number of variables concerned
 * The swap can associated with a delta: the difference in the objective value
 * obtained when the swap is performed on a candidate solution.
 */
public class Swap implements Comparable<Swap> {

    public final int[] variableIds;
    public HashSet<Integer> variableIdsSet;
    private int delta;

    public Swap(int... variableIds) {
        this.variableIds = variableIds;
        this.delta = 0;

        this.variableIdsSet = new HashSet<>();
        for (int variableId : variableIds) {
            this.variableIdsSet.add(variableId);
        }
    }

    public int getDelta() {
        return this.delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    /**
     * Returns true if the swap contains the variable with the given ID
     * @param variableId the ID of the variable to check
     * @return true if the swap contains the variable with the given ID
     */
    public boolean contains(int variableId) {
        return this.variableIdsSet.contains(variableId);
    }

    /**
     * Creates an extension of this swap with the variable with the given ID
     * added to the last position of the swap
     * @param variableId the ID of the variable to add
     * @return the extended swap
     */
    public Swap getExtendedSwapWith(int variableId) {
        int[] newVariableIds = new int[this.variableIds.length + 1];
        System.arraycopy(this.variableIds, 0, newVariableIds, 0, this.variableIds.length);
        newVariableIds[this.variableIds.length] = variableId;
        return new Swap(newVariableIds);
    }

    @Override
    public int compareTo(Swap other) {
        return this.delta - other.delta; // to sort the swaps by increasing delta values (we want to minimize the cost)
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int variableId : this.variableIds) {
            builder.append(variableId).append(' ');
        }
        return builder.toString();
    }
}
