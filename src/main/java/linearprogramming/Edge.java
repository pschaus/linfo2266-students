package linearprogramming;

import java.util.Objects;

/**
 * undirected edge within a graph
 */
public class Edge implements Comparable<Edge> {

    private final int v;
    private final int w;

    /**
     * create an edge, with v < w
     * @param v first node of the edge
     * @param w second node of the edge
     */
    public Edge(int v, int w) {
        if (v < w) {
            this.v = v;
            this.w = w;
        }
        else {
            this.v = w;
            this.w = v;
        }
    }

    public Edge(Edge e) {
        this.v = e.v();
        this.w = e.w();
    }

    public int v() {
        return v;
    }

    public int w() {
        return w;
    }

    @Override
    public int compareTo(Edge that) {
        if (this.v < that.v) return -1;
        if (this.v > that.v) return +1;
        if (this.w < that.w) return -1;
        if (this.w > that.w) return +1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return v == edge.v && w == edge.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v, w);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "v=" + v +
                ", w=" + w +
                '}';
    }
}
