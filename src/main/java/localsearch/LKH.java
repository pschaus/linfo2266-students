package localsearch;

import util.Pair;
import util.tsp.TSPInstance;

import java.util.Arrays;

public class LKH {

    private TSPInstance instance;

    public LKH(TSPInstance instance) {
        this.instance = instance;
    }

    /**
     * Convert a tour representation to a successor representation
     *
     * @param c a candidate solution
     * @return an array of successors
     */
    int[] tourToSucc(Candidate c) {
        // TODO implement the method that converts a tour representation to a successor representation
         throw new util.NotImplementedException("LKH.tourToSucc");
    }

    /**
     * Reverse the path from b to c in the successor representation
     * and connect b to d
     *
     * @param succ successor representation of the current tour
     * @param b    start of the path to reverse
     * @param c    end of the path to reverse
     * @param d    node to connect b to
     */
    void reversePath(int[] succ, int b, int c, int d) {
        int i = b;
        int si = succ[b];
        succ[b] = d;
        while (i != c) {
            int temp = succ[si];
            succ[si] = i;
            i = si;
            si = temp;
        }
    }

    /**
     * Convert a successor representation to a tour representation
     * the tour starts from node 0
     * e.g., succ = [2,0,1] -> tour = [0,2,1]
     *
     * @param succ
     * @return
     */
    int[] succToTour(int[] succ) {
        // TODO implement the method that converts a successor representation to a tour representation
         throw new util.NotImplementedException("LKH.succToTour");
    }



    /**
     * Apply the Lin-Kernighan-Helsgaun heuristic to improve the candidate solution
     *
     * @param candidate
     * @return an improved candidate solution
     */
    public Candidate applyLKH(Candidate candidate) {
        // TODO implement the Lin-Kernighan-Helsgaun heuristic to improve the candidate solution using the methods defined above
         throw new util.NotImplementedException("LKH.applyLKH");
    }
}
