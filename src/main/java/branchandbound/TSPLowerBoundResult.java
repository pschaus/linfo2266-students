package branchandbound;

import java.util.List;

public class TSPLowerBoundResult {

    private final double lb;
    private final List<Edge> edges;

    public TSPLowerBoundResult(double lb, List<Edge> edges) {
        this.lb = lb;
        this.edges = edges;
    }

    public List<Edge> edges() {
        return edges;
    }

    public double lb() {
        return lb;
    }

}