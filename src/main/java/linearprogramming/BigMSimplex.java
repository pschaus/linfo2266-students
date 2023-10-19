package linearprogramming;

import java.util.function.Consumer;

/**
 * Solves a problem formulated as
 *
 * max cx
 * s.t.
 *  Ax <= b
 *  x  >= 0
 *
 * By introducing auxiliary variables x_a, changing the problem into
 *
 * max cx - M x_a
 * s.t.
 *  Ax + x_a <= b
 *  x        >= 0
 *  x_a      >= 0
 * The basis is found by setting x_a = b
 *
 * Because the objective needs to be maximized, and because the x_a are positive,
 * the best objective value should be found with x_a == 0
 */
public class BigMSimplex {

    private static final double EPSILON = 1.0E-8;
    public double[][] a;   // tableaux

    private int m;          // number of constraints
    private int n;          // number of original variables
    private double BigM;    // constant used in the big M formulation
    private int[] basis;    // basis[i] = basic variable corresponding to row i

    /**
     *
     * @param A left hand side of the inequalities
     * @param b right hand side of the inequalities
     * @param c cost for the objective
     * @param BigM constant used in the bigM formulation
     */
    public BigMSimplex(double[][] A, double[] b, double[] c, double BigM) {
        this(A, b, c, BigM, i -> {});
    }

    /**
     *
     * @param A left hand side of the inequalities
     * @param b right hand side of the inequalities
     * @param c cost for the objective
     * @param BigM constant used in the bigM formulation
     * @param listener listener called in the solve method
     */
    public BigMSimplex(double[][] A, double[] b, double[] c, double BigM, Consumer<double[][]> listener) {
        m = b.length;
        n = c.length;
        a = new double[m+1][n+m+m+1];
        this.BigM = BigM;
        // TODO fill the tableau a using the provided matrices
        //  your code should look a lot like the code from TwoPhaseSimplex
        //  find the differences between TwoPhaseSimplex and the Big-M approach to fill correctly the matrix
        //  you should not modify the dimensions of the matrix, nor touching the other methods provided

        solve(listener);
    }

    // run simplex algorithm starting from initial BFS
    // WARNING: you should not modify this code
    private void solve(Consumer<double[][]> listener) {
        listener.accept(a); // listener called before starting the solving.
        while (true) {

            // find entering column q
            int q = bland();
            if (q == -1) break;  // optimal

            // find leaving row p
            int p = minRatioRule(q);
            if (p == -1) throw new ArithmeticException("Linear program is unbounded");

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
            listener.accept(a); // listener called before starting anything
        }
    }

    // lowest index of a non-basic column with a positive cost - using artificial objective function
    private int bland() {
        for (int j = 0; j < n+m; j++)
            if (a[m][j] > EPSILON) return j;
        return -1;  // optimal
    }

    // find row p using min ratio rule (-1 if no such row)
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < m; i++) {
            // if (a[i][q] <= 0) continue;
            if (a[i][q] <= EPSILON) continue;
            else if (p == -1) p = i;
            else if ((a[i][n+m+m] / a[i][q]) < (a[p][n+m+m] / a[p][q])) p = i;
        }
        return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        // everything but row p and column q
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n+m+m; j++)
                if (i != p && j != q) a[i][j] -= a[p][j] * (a[i][q] / a[p][q]);

        // zero out column q
        for (int i = 0; i <= m; i++)
            if (i != p) a[i][q] = 0.0;

        // scale row p
        for (int j = 0; j <= n+m+m; j++)
            if (j != q) a[p][j] /= a[p][q];
        a[p][q] = 1.0;
    }

    // return optimal objective value
    public double value() {
        return -a[m][n+m+m];
    }

    // return primal solution vector
    public double[] primal() {
        double[] x = new double[n];
        for (int i = 0; i < m; i++)
            if (basis[i] < n) x[basis[i]] = a[i][n+m+m];
        return x;
    }

    // return dual solution vector
    public double[] dual() {
        double[] y = new double[m];
        for (int i = 0; i < m; i++) {
            y[i] = -a[m][n+i];
            if (y[i] == -0.0) y[i] = 0.0;
        }
        return y;
    }

    // print tableaux
    public void show() {
        System.out.println("m = " + m);
        System.out.println("n = " + n);
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n+m+m; j++) {
                System.out.printf("%7.2f ", a[i][j]);
                if (j == n+m-1 || j == n+m+m-1) System.out.print(" |");
            }
            System.out.println();
        }
        System.out.print("basis = ");
        for (int i = 0; i < m; i++)
            System.out.print(basis[i] + " ");
        System.out.println();
        System.out.println();
    }

}
