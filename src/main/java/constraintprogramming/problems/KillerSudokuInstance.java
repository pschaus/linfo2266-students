package constraintprogramming.problems;

import java.util.Arrays;
import java.util.StringJoiner;

public class KillerSudokuInstance {

    private final KillerSudokuGroup[] groups;
    private final Coordinate[][] values;
    private final int n;

    public KillerSudokuInstance(int n, KillerSudokuGroup[] groups) {
        double subSquares = Math.sqrt(n);
        if (Math.abs(subSquares - ((int) subSquares)) > 1e-6) {
            throw new IllegalArgumentException("Invalid size");
        }
        this.groups = groups;
        this.n = n;
        values = new Coordinate[n][n];
        boolean[][] seen = new boolean[n][n];
        for (KillerSudokuGroup g: groups) {
            for (Coordinate c: g.values()) {
                values[c.i()][c.j()] = c;
                if (seen[c.i()][c.j()]) {
                    throw new IllegalArgumentException("Cell (" + c.i() + "," + c.j() + ") is covered by more than one group");
                }
                seen[c.i()][c.j()] = true;
            }
        }
        for (int i = 0 ; i < n ; ++i) {
            for (int j = 0 ; j < n ; ++j) {
                if (!seen[i][j]) {
                    throw new IllegalArgumentException("Cell (" + i + "," + j + ") is not covered by any group");
                }
            }
        }
    }

    /**
     * Tells if there is an initial value on one cell
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return True if there is an initial value at (x,y)
     */
    public boolean isValue(int x, int y) {
        return values[x][y].isValue();
    }

    /**
     * Returns the initial value of the problem on one tiny square
     * A returned value of 0 means that there is no initial value encoded at this position
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return Initial value located at (x,y). 0 Means that there is no initial value
     */
    public int value(int x, int y) {
        return values[x][y].value();
    }

    /**
     * Gives all groups belonging to the instance
     * All (x,y) coordinates within the problems are covered by the group
     *
     * @return all groups belonging to the instance
     */
    public KillerSudokuGroup[] groups() {
        return groups;
    }

    /**
     * Size of the killer sudoku
     *
     * @return size of the killer sudoku
     */
    public int n() {
        return n;
    }

    @Override
    public String toString() {
        int[][] values = new int[n][n];
        for (int i = 0; i < n ; ++i) {
            for (int j = 0 ; j < n ; ++j) {
                values[i][j] = value(i, j);
            }
        }
        return toString(values);
    }

    /**
     * Encodes a solution to a killer sudoku instance
     * No check is done here: a wrong solution could be encoded!
     * Refer to the unit tests to ensure that a solution is valid
     */
    public static class Solution {

        public final int[][] values;

        public Solution(int[][] values) {
            int n = values.length;
            this.values = values;
        }

        public final int value(int x, int y) {
            return values[x][y];
        }

        public final int n() {
            return values.length;
        }

        @Override
        public String toString() {
            return KillerSudokuInstance.toString(values);
        }
    }

    /**
     * Gives the string representation of a killer sudoku, omitting the representation of the sums
     *
     * @param values number within the killer sudoku. 0 means no number
     * @return String representation of a killer sudoku
     */
    private static String toString(int[][] values) {
        int n = values.length;
        StringJoiner line;
        StringJoiner square = new StringJoiner("\n");
        int maxDecimal = (int) Math.log10(n) + 1;
        String format = "%" + maxDecimal + "d";
        char[] chars = new char[maxDecimal];
        Arrays.fill(chars, '*');
        int subSquare = (int) Math.sqrt(n);
        String emptyString = new String(chars);
        int spacing = subSquare*maxDecimal + subSquare+1;
        chars = new char[spacing*subSquare];
        Arrays.fill(chars, '-');
        for (int sub = 1 ; sub < subSquare ; ++sub) {
            if (sub == 1) {
                chars[sub * spacing - 1] = '+';
            } else {
                chars[sub * spacing] = '+';
            }
        }
        String rowSeparator = new String(chars);
        for (int i = 0; i < n ; ++i) {
            if (i != 0 && i % subSquare == 0) {
                square.add(rowSeparator);
            }
            line = new StringJoiner(" ");
            for (int j = 0; j < n; ++j) {
                if (j != 0 && j%subSquare == 0) {
                    line.add("|");
                }
                if (values[i][j] == 0)
                    line.add(emptyString);
                else
                    line.add(String.format(format, values[i][j]));
            }
            square.add(line.toString());
        }
        return square.toString();
    }
}
