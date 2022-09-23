package branchandbound;

import java.util.List;

public class OneTreeResult {

    private final double lb;
    private final List<Edge> edges;

    public OneTreeResult (double lb, List<Edge> edges) {
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