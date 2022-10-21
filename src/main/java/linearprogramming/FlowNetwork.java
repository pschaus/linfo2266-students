package linearprogramming;

/******************************************************************************
 *  Compilation:  javac FlowNetwork.java
 *  Execution:    java FlowNetwork V E
 *  Dependencies: FlowEdge.java
 *
 *  A capacitated flow network, implemented using adjacency lists.
 *
 ******************************************************************************/


import util.InputReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *  The {@code FlowNetwork} class represents a capacitated network
 *  with vertices named 0 through <em>V</em> - 1, where each directed
 *  edge is of type {@link FlowEdge} and has a real-valued capacity
 *  and flow.
 *  It supports the following two primary operations: add an edge to the network,
 *  iterate over all of the edges incident to or from a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which
 *  is a vertex-indexed array of {@link LinkedList} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident to a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/64maxflow">Section 6.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class FlowNetwork {

    private final int V;
    private int E;
    private Queue<FlowEdge>[] adj;
    private ArrayList<Integer> sources = new ArrayList<>();
    private ArrayList<Integer> sinks = new ArrayList<>();

    /**
     * Returns a deep copy of a network
     *
     * @param network flow network to copy
     */
    public FlowNetwork(FlowNetwork network) {
        this(network.V);
        addSource(network.source());
        addSink(network.sink());
        for (FlowEdge edge: network.edges()) {
            addEdge(new FlowEdge(edge));
        }
    }

    /**
     * Initializes an empty flow network with {@code V} vertices and 0 edges.
     * @param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public FlowNetwork(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Graph must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (Queue<FlowEdge>[]) new LinkedList[V];
        for (int v = 0; v < V; v++)
            adj[v] = new LinkedList<FlowEdge>();
    }

    /**
     * Initializes a random flow network with {@code V} vertices and <em>E</em> edges.
     * The capacities are integers between 0 and 99 and the flow values are zero.
     * @param V the number of vertices
     * @param E the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
    public FlowNetwork(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        Random random = new Random();
        for (int i = 0; i < E; i++) {
            int v = random.nextInt(V);
            int w = random.nextInt(V);
            double capacity = random.nextInt(100);
            addEdge(new FlowEdge(v, w, capacity));
        }
    }


    /**
     * Initializes a random acyclic flow network with {@code V} vertices and <em>E</em> edges.
     * The capacities are integers between 0 and 99 and the flow values are zero.
     * @param V the number of vertices
     * @param E the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
    public FlowNetwork(int V, int E, int source, int sink) {
        this(V);
        int threshold = 100;
        addSource(source);
        addSink(sink);
        Random random = new Random();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = random.nextInt(V);
            int w = random.nextInt(V);
            if (v == sink || w == source || v == w)
                i -= 1;
            else if (edge(v, w) != null) {
                i -= 1; // an edge already exists
            } else {
                int maxCapa = v == source || w == sink ? 3 * threshold : threshold;
                double capacity = 5 + random.nextInt(maxCapa);
                addEdge(new FlowEdge(v, w, capacity));
            }
        }
    }


    /**
     * Initializes a flow network from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge capacities,
     * with each entry separated by whitespace.
     * @param in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public FlowNetwork(InputReader in) {
        this(in.getInt());
        int E = in.getInt();
        if (E < 0) throw new IllegalArgumentException("number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = in.getInt();
            int w = in.getInt();
            validateVertex(v);
            validateVertex(w);
            double capacity = in.getDouble();
            addEdge(new FlowEdge(v, w, capacity));
        }
    }

    /**
     * Initializes a FlowNetwork from an instance file
     * The instance must be written within the DIMACS format (http://lpsolve.sourceforge.net/5.5/DIMACS_maxf.htm)
     * @param instancePath path to where the instance is written
     * @return FlowNetwork from the file
     */
    public static FlowNetwork fromFile(String instancePath) {
        InputReader reader = new InputReader(instancePath);
        // parse the file until the right number of edges has been met
        int V = Integer.MAX_VALUE;
        int current_V = 0;
        FlowNetwork network = null;
        while (current_V != V) {
            char lineType = reader.getChar();
            switch (lineType) {
                case 'c':  // comment line, ignored
                    reader.nextLine();
                    break;
                case 'p': // problem line, format "max E V"
                    String problemType = reader.getString();
                    int E = reader.getInt();
                    V = reader.getInt();
                    network = new FlowNetwork(E);
                    break;
                case 'n': // node descriptor, specify source or destination, format "n 1 s"
                    int nodeId = reader.getInt() - 1;  // we are civilized and begin counting at 0
                    char nodeType = reader.getChar();
                    assert network != null;
                    if (nodeType == 's') {
                        network.addSource(nodeId);
                    } else {
                        network.addSink(nodeId);
                    }
                    break;
                case 'a': // arc descriptor, format "a 1 3 0"
                    int from = reader.getInt() - 1; // we are civilized and begin counting at 0
                    int to = reader.getInt() - 1;
                    int capacity = reader.getInt();
                    assert network != null;
                    FlowEdge edge = new FlowEdge(from, to, capacity);
                    network.addEdge(edge);
                    ++current_V;
                    break;
                default:
                    throw new IllegalArgumentException("invalid line type encountered" + lineType);
            }
        }
        return network;
    }

    public void toDimacs() {
        String filename = String.format("data/Flow/flow%04d_%04d.txt", V, E);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(String.format("p max %d %d\n", V, E));
            writer.write(String.format("n %d s\n", source() + 1));
            writer.write(String.format("n %d t\n", sink() + 1));
            for (FlowEdge e: edges()) {
                writer.write(String.format("a %d %d %d\n", e.from() + 1, e.to() + 1, (int) e.capacity()));
            }
            writer.write("c End of file");
            writer.close();
        } catch (IOException ignored) {
            System.err.println("error when creating writer for" + filename);
            return;
        }
    }

    /**
     * Returns the number of vertices in the edge-weighted graph.
     * @return the number of vertices in the edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the edge-weighted graph.
     * @return the number of edges in the edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the edge {@code e} to the network.
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between
     *         {@code 0} and {@code V-1}
     */
    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    /**
     * Returns the edges incident on vertex {@code v} (includes both edges pointing to
     * and from {@code v}).
     * @param v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<FlowEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the edges incident on vertex {@code v} (edges pointing from {@code v}).
     * @param v the vertex
     * @return the edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<FlowEdge> adjFrom(int v) {
        validateVertex(v);
        Stream<FlowEdge> targetStream = StreamSupport.stream(adj[v].spliterator(), false);
        return targetStream.filter(edge -> edge.from() == v).collect(Collectors.toList());
    }

    /**
     * Gives the list of all edges excluding self loops
     * @return list of all edges, excluding self loops
     */
    public Iterable<FlowEdge> edges() {
        Queue<FlowEdge> list = new LinkedList<FlowEdge>();
        for (int v = 0; v < V; v++)
            for (FlowEdge e : adj(v)) {
                if (e.to() != v)
                    list.add(e);
            }
        return list;
    }

    /**
     * Gives the edge passing from one node and going to another one
     * Returns null if the edge does not exist
     *
     * @param from origin of the node
     * @param to destination of the node
     * @return edge passing through the two nodes
     */
    public FlowEdge edge(int from, int to) {
        if (from < 0 || from >= E || to < 0 || to >= E)
            return null;
        for (FlowEdge e: adj[from]) {
            if (e.to() == to)
                return e;
        }
        return null;
    }

    /**
     * Adds a set of sources to the flow network
     * @param sources sources that must be added to the network
     */
    public void addSource(int... sources) {
        for (int source: sources) {
            validateVertex(source);
            this.sources.add(source);
        }
    }

    /**
     * Adds a set of sinks to the flow network
     * @param sinks sinks that must be added to the network
     */
    public void addSink(int... sinks) {
        for (int sink: sinks) {
            validateVertex(sink);
            this.sinks.add(sink);
        }
    }

    /**
     * Retrieves all sources from the network
     * @return all sources from the network
     */
    public ArrayList<Integer> sources() {
        return sources;
    }

    /**
     * Retrieves the first source from the flow network
     * @return first source from the network
     */
    public int source() {
        return sources.get(0);
    }

    /**
     * Retrieves all sinks from the flow network
     * @return all sinks from the flow network
     */
    public ArrayList<Integer> sinks() {
        return sinks;
    }

    /**
     * Retrieves the first sink from the flow network
     * @return first sink from the network
     */
    public int sink() {
        return sinks.get(0);
    }

    /**
     * Gives the number of edges outgoing from this node
     * @param node valid node in the network
     * @return
     */
    public int nEdgesFrom(int node) {
        int n = 0;
        for (FlowEdge e : adj[node]) {
            if (e.from() == node)
                n++;
        }
        return n;
    }

    /**
     * Gives the number of edges ingoing to this node
     * @param node valid node in the network
     * @return
     */
    public int nEdgesTo(int node) {
        int n = 0;
        for (FlowEdge e : adj[node]) {
            if (e.from() != node)
                n++;
        }
        return n;
    }


    /**
     * Returns a string representation of the flow network.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *    followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adj[v]) {
                if (e.to() != v) s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        s.append("s: [").append(sources.stream().map(Object::toString).collect(Collectors.joining(", ")))
                .append(']').append(NEWLINE);
        s.append("t: [").append(sinks.stream().map(Object::toString).collect(Collectors.joining(", ")))
                .append(']').append(NEWLINE);
        return s.toString();
    }

}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
