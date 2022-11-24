package constraintprogramming.problems;

/**
 * Value within a cell of a killer sudoku
 */
public class Coordinate {

    private int i;
    private int j;
    private int v; // value located at (i,j)

    /**
     * 1st coordinate of the cell
     *
     * @return 1st coordinate of the cell
     */
    public int i() {
        return i;
    }

    /**
     * 2nd coordinate of the cell
     *
     * @return 2nd coordinate of the cell
     */
    public int j() {
        return j;
    }

    /**
     * Value contained within the cell
     * A value of 0 means that there is no assigned value (yet) to this cell
     *
     * @return value contained within the cell, 0 means no assigned value
     */
    public int value() {
        return v;
    }

    /**
     * Tells if there is an initial value at the coordinate cell
     *
     * @return True if there is an initial value
     */
    public boolean isValue() {
        return v != 0;
    }

    public Coordinate(int i, int j, int v) {
        this.i = i;
        this.j = j;
        this.v = v;
    }

    public static Coordinate[][] fromMatrix(int[][] values) {
        int n = values.length;
        Coordinate[][] c = new Coordinate[n][n];
        for (int i = 0 ; i < n ; ++i) {
            for (int j = 0 ; j < n ; ++j) {
                c[i][j] = new Coordinate(i, j, values[i][j]);
            }
        }
        return c;
    }

}
