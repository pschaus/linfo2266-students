package constraintprogramming.problems;

public class KillerSudokuGroup {

    private final int sum;
    private final Coordinate[] values;

    public KillerSudokuGroup(int sum, Coordinate... values) {
        this.values = values;
        this.sum = sum;
    }

    /**
     * Array of entries related to the group
     *
     * @return array of entries related to the group
     */
    public Coordinate[] values() {
        return values;
    }

    /**
     * The value that needs to be reached when summing all {@link Coordinate} entries within the group
     *
     * @return sum of all entries within the group
     */
    public int sum() {
        return sum;
    }

    /**
     * Number of entries related to this group
     *
     * @return number of entries related to the group
     */
    public int size() {
        return values.length;
    }

}
