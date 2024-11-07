package localsearch;

/**
 * Interface for selecting a neighbor of a solution
 */
public interface NeighborSelection {
    public Candidate getNeighbor(Candidate candidate);
}
