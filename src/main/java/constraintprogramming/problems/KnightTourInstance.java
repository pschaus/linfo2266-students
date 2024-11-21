package constraintprogramming.problems;

/**
 * A knight's tour is a sequence of moves of a knight on a chessboard
 * such that the knight visits every square exactly once and returns to the starting square.
 * https://en.wikipedia.org/wiki/Knight%27s_tour
 *
 * An instance of the problem can force the knight
 * to be at specific positions at specific timesteps.
 */
public class KnightTourInstance {

    private final int n; //  n x n chessboard

    // An n*n array containing the preset moves
    // in the array the ith value correspond to the ith move in the tour
    // that is the position of the knight at the ith timestep.
    // The first value in the array thus correspond to the starting point.
    // If the moves is not restricted, the corresponding value is -1
    private final int[] moves;

    /**
     * Return the ith move
     * @param i the timestep
     * @return the move
     */
    public int move(int i) {return moves[i];}

    /**
     * Tells if there is an initial move at the queried timestep
     * @param i
     * @return
     */
    public boolean isMove(int i) {return moves[i] != -1;}

    /**
     * Size of the grid
     *
     * @return the size of the grid
     */
    public int n() {return n;}

    @Override
    public String toString() {return toString(this.moves);}

    public KnightTourInstance(int n, int[] moves) {
        this.n = n;
        this.moves = moves;
    }

    public static class Solution {
        public final int[] moves;

        public Solution(int[] moves) {
            this.moves = moves;
        }

        public int move(int i) {return moves[i];}

        @Override
        public String toString() {return KnightTourInstance.toString(moves);}
    }

    private static String toString(int[] moves) {
        StringBuilder sb = new StringBuilder();
        for (int move : moves) {
            if (move == -1) sb.append(". ");
            else sb.append(move).append(" ");
        }
        return sb.toString();
    }
}
