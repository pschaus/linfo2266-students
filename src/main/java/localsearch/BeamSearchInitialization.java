package localsearch;

import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * Beam a TSP using a beam search algorithm
 */
public class BeamSearchInitialization extends Initialization {

    private int beamWidth;

    public BeamSearchInitialization(TSPInstance tsp, int beamWidth) {
        super(tsp);
        this.beamWidth = beamWidth;
    }

    @Override
    public Candidate getInitialSolution() {
        // TODO
         throw new util.NotImplementedException("BeamSearchInitialization.getInitialSolution");
    }

}
