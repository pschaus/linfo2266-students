package localsearch;


/**
 * Select the first neighbor that improves the solution,
 * if no neighbor improves the solution, return the original solution
 */
public class FirstSelection implements NeighborSelection {

    @Override
    public Candidate getNeighbor(Candidate candidate) {
        for (int i = 0; i < candidate.getTour().size() - 1; i++) {
            // move between index and index + 1 is useless
            for (int j = i + 2; j < candidate.getTour().size(); j++) {
                if (candidate.twoOptDelta(i, j) < 0) {
                    candidate.twoOpt(i, j);
                    return candidate;
                }
            }
        }
        return candidate;
    }
}
