package localsearch;

import util.Pair;
import util.tsp.TSPInstance;

import java.util.HashSet;

/**
 * Select the best neighbor that is not tabu
 */
public class BestWithTabuSelection implements NeighborSelection {

    int iteration = 0;
    private int tabuSize; // size of the tabu list
    int[][] tabu; // tabu[i][j] is the next iteration when the edge (i,j) become non-tabu

    public BestWithTabuSelection(int tabuSize, TSPInstance tsp) {
        iteration = 0;
        this.tabuSize = tabuSize;
        tabu = new int[tsp.n][tsp.n];
    }


    public void addTabu(int i, int j) {
        tabu[i][j] = iteration + tabuSize;
    }

    public boolean isTabu(int i, int j) {
        return iteration < tabu[i][j];
    }

    /**
     * Selects the best non-tabu 2Opt neighbor
     * It should never return the same candidate as the one given in argument
     * The first removed edge in the selected move becomes tabu
     * @param candidate
     * @return the best non-tabu 2Opt neighbor, the first removed edge in the selected move becomes tabu
     */
    @Override
    public Candidate getNeighbor(Candidate candidate) {
        iteration++;
         throw new util.NotImplementedException("SelectionTabu.getNeighbor");
    }

}

