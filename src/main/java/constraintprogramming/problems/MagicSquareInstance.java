package constraintprogramming.problems;

import java.util.Arrays;
import java.util.StringJoiner;

public class MagicSquareInstance {

    private final int n;   // an instance is a n*n square
    // sum of each row, column and the main diagonal.
    // 0 == no assigned sum, you have to search for one
    private int[][] values;

    /**
     * Returns the initial value of the problem on one tiny square
     * A returned value of 0 means that there is no initial value encoded at this position
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return Initial value located at (x,y). 0 Means that there is no initial value
     */
    public int value(int x, int y) {
        return values[x][y];
    }

    /**
     * Tells if there is an initial value on one tiny square
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return True if there is an initial value at (x,y)
     */
    public boolean isValue(int x, int y) {
        return values[x][y] != 0;
    }

    /**
     * Size of the magic square
     *
     * @return size of the magic square
     */
    public int n() {
        return n;
    }

    public MagicSquareInstance(int[][] values) {
        this.n = values.length;
        this.values = values;
    }

    @Override
    public String toString() {
        return toString(values, false);
    }

    /**
     * Encodes a solution to a MagicSquareInstance
     * No check is done here: a wrong solution could be encoded!
     * Refer to the unit tests to ensure that a solution is valid
     */
    public static class Solution {

        public final int[][] values;
        private final int n;

        public Solution(int[][] values) {
            n = values.length;
            this.values = values;
        }

        public final int value(int x, int y) {
            return values[x][y];
        }

        public final int n() {
            return n;
        }

        @Override
        public String toString() {
            return MagicSquareInstance.toString(values, true);
        }

    }

    /**
     * Gives the string representation of a magic square, possibly adding the sum of the rows, columns and the 1st diagonal
     *
     * @param values number within the magic square. 0 means no number
     * @param writeSum if true, write the sum of the rows, columns and the 1st diagonal
     * @return String representation of a magic square
     */
    public static String toString(int[][] values, boolean writeSum) {
        StringJoiner line;
        StringJoiner square = new StringJoiner("\n");
        int n = values.length;
        int maxNumber = n*n;
        int maxDecimal;
        if (writeSum) {
            int maxSum = maxNumber * (maxNumber+1) / 2; // rough and inexact upper bound, do not care about the actual value
            maxDecimal = (int) Math.log10(maxSum) + 1;
        }
        else {
            maxDecimal = (int) Math.log10(maxNumber) + 1;
        }
        String format = "%" + maxDecimal + "d";
        char[] chars = new char[maxDecimal];
        Arrays.fill(chars, '*');
        String emptyString = new String(chars);
        int[] colSum = new int[n];
        int diagSum = 0;
        for (int i = 0 ; i < n ; ++i) {
            line = new StringJoiner(" ");
            int sum = 0;
            for (int j = 0 ; j < n ; ++j) {
                if (values[i][j] == 0)
                    line.add(emptyString);
                else
                    line.add(String.format(format, values[i][j]));
                sum += values[i][j];
                if (writeSum) {
                    colSum[j] += values[i][j];
                    if (i == j)
                        diagSum += values[i][j];
                }
            }
            if (writeSum)
                line.add("| " + sum);
            square.add(line.toString());
        }
        if (writeSum) {
            chars = new char[maxDecimal*(n+2) + n];
            Arrays.fill(chars, '-');
            String delimiter = new String(chars);
            square.add(delimiter);
            line = new StringJoiner(" ");
            for (int i = 0; i < n; ++i) {
                line.add(String.format(format, colSum[i]));
            }
            line.add(String.format("| %d", diagSum));
            square.add(line.toString());
        }
        return square.toString();
    }
}
