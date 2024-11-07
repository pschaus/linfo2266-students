package localsearch;

import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Initializes with a tour using the Pilot method
 */
public class PilotInitialization extends BeamSearchInitialization {


    public PilotInitialization(TSPInstance tsp) {
        super(tsp, 1);
    }

    // TODO: you may want to override a method here


}
