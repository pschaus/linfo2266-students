package astar.solver;

public class SearchStatistics {

    public long timeMs;
    public int nodesExpanded;

    public SearchStatistics(long timeMs, int nodesExpanded) {
        this.timeMs = timeMs;
        this.nodesExpanded = nodesExpanded;
    }

    @Override
    public String toString() {
        return "\n\ttimeMs = " + timeMs +
                "\n\tnodesExpanded = " + nodesExpanded +
                "\n";
    }

}
