package localsearch;

import util.tsp.TSPInstance;
import java.util.*;

public class BeamSearchInsert extends BeamSearchInitialization {

    public BeamSearchInsert(TSPInstance tsp, int beamWidth) {
        super(tsp, beamWidth);
    }

    /**
     * Defines how to expand a partial solution by inserting each unvisited city at every possible position
     * @param s
     * @return a list of partial solutions
     */
    @Override
    protected List<PartialSolution> expand(PartialSolution s) {
        // TODO implement a method to expand a partial solution by inserting each unvisited city at every possible position
         throw new util.NotImplementedException("BeamSearchInsert.expand");
    }
}
