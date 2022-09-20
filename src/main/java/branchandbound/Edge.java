package branchandbound;

public record Edge(int v1, int v2, double cost) implements Comparable<Edge> {

    @Override
    public String toString() {
        return "("+v1+" - "+v2+") cost:"+cost;
    }

    @Override
    public int compareTo(Edge o) {

        return Double.compare(this.cost, o.cost);
    }
}

