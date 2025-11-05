package localsearch;

import util.tsp.TSPInstance;
import java.util.*;

public class BeamSearchAppend extends BeamSearchInitialization {

    public BeamSearchAppend(TSPInstance tsp, int beamWidth) {
        super(tsp, beamWidth);
    }

    /**
     * Defines how to expand a partial solution by appending each unvisited city at the end
     * @param s a partial solution
     * @return a list of partial solutions
     */
    @Override
    protected List<PartialSolution> expand(PartialSolution s) {
        // TODO implement a method that expands a partial solution by appending remaining cities at the end
         throw new util.NotImplementedException("BeamSearchAppend.expand");
    }
}
