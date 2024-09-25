package competition;

import java.util.BitSet;

import static competition.Utils.hashPair;

public class Path {
    public final int src; // the source node of the path
    public final int dest; // the destination of the path

    // The set of nodes crossed by the path is represented by a bitset
    // of size n, where n is the number of nodes in the graph
    // The bit at index i is - true iff the node i is crossed by the path
    //                       - false otherwise
    public final BitSet nodes;

    public Path(int src, int dest, BitSet nodes) {
        this.src = src;
        this.dest = dest;
        this.nodes = nodes;
    }

    /**
     * @param nodeA index of node A
     * @param nodeB index of node B
     * @return true if the path allows to distinguish the two given nodes,
     *         false otherwise
     */
    public boolean distinguish(int nodeA, int nodeB) {
        return ((nodes.get(nodeA) && !nodes.get(nodeB)) ||
                (!nodes.get(nodeA) && nodes.get(nodeB)));
    }

    /**
     * Compute the list of pair of nodes that the path makes distinguishables.
     * This list is encoded as a bitset of size (n*(n-1)/2), i.e. the number
     * of pair of nodes than can be constituted.
     * Each pair has an index given by the hashPair method, and the bit at index i
     * is true iff the corresponding pair (a, b) can be distinguished by the path,
     * i.e. if the path crosses 'a' but not 'b', or 'b' and not 'a'.
     * @param n the number of nodes in the graph
     * @return a bitset containing the pair of nodes distinguished by the path
     */
    public BitSet getDistinguishedSet(int n) {
        BitSet pairs = new BitSet(n * (n - 1) / 2);
        for (int nodeA = 0; nodeA < n; nodeA++) {
            if (nodes.get(nodeA)) {
                for (int nodeB = 0; nodeB < nodeA; nodeB++) {
                    if (!nodes.get(nodeB)) {
                        pairs.set(hashPair(nodeA, nodeB, n), true);
                    }
                }
                for (int nodeB = nodeA + 1; nodeB < n; nodeB++) {
                    if (!nodes.get(nodeB)) {
                        pairs.set(hashPair(nodeA, nodeB, n), true);
                    }
                }
            }
        }
        return pairs;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%d %d |", src, dest));
        for (int node = 0; node < nodes.length(); node++) {
            if (nodes.get(node)) {
                str.append(String.format(" %d", node));
            }
        }
        return str.toString();
    }
}
