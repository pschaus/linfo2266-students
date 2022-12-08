package util.decarbonation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an instance of the maximum decarbonation problem
 * where you want to find the set of places where to safely build nuclear 
 * power plants.
 */
public final class MaximumDecarbonationInstance {
    /** 
     * That's basically our adjacency matrix, but we're only using one bit
     * per neighbor rather than a complete int (32bits).
     */
    private final BitSet[] graph;

    /** 
     * Creates a new instance of the problem with n candidate build sites.
     * 
     * Note: 
     * You do not call this method yourself. You need to either of the two
     * static builder methods defined a bit later (fromString and fromFile).
     * 
     * @param n the number of candidate build sites that have been identified
     */
    private MaximumDecarbonationInstance(final int n) {
        this.graph = new BitSet[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new BitSet(n);
            graph[i].set(i); // these graphs are reflexive: you are always a neighbor of yourself
        }
    }
    /** @return the number of potential build sites to place a nuclear plant */
    public int nbSites() {
        return graph.length;
    }
    /** 
     * @return all the neighbors of the node a (range 1-n)
     * @param a the identifier of one node (in the range 1..n: conversion is done internally)
     */
    public BitSet neighbors(final int a) {
        return graph[id(a)];
    }
    /** 
     * @return true iff it is safe to build a power plant at both sites a and b
     * @param a the identifier of one node (in the range 1..n: conversion is done internally)
     * @param b the identifier of one node (in the range 1..n: conversion is done internally)
     */
    public boolean isSafe(final int a, final int b) {
        return !graph[id(a)].get(id(b));
    }
    

    /** 
     * Draws an edge between the nodes a and b.
     * 
     * Note: 
     * For conveniency, and because the instance input files use locations 
     * numbered from 1 to n, the identifiers a and b are also numbered from
     * 1 to n. The conversion is done internally by this class.
     * 
     * @param a the identifier of the first node (in the range 1-n)
     * @param b the identifier of the second node (in the range 1-n)
     */
    private void drawEdge(final int a, final int b) {
        int ida = id(a);
        int idb = id(b);

        graph[ida].set(idb);
        graph[idb].set(ida);
    }
    /** 
     * Returns the internal id (0 based) of the given node identifier
     * @param a the node id
     */
    private int id(int a) {
        return a-1;
    }

    /**
     * Parses the input text and returns a corresponding instance.
     * 
     * The instances are structured as follows
     * ```
     * c any line starting with the character 'c' is a comment and can be ignored
     * c
     * c The header line "p edge x y" tells the basic info required to parse the 
     * c file. It states how many nodes and edges there are in the graph.
     * c (If you are aware, this is the standard DIMACS edge format).
     * c
     * c Then any subsequent line starts with an 'e' to indicate that it represents
     * c an edge of the graph (this is an edge list representation).
     * c
     * p edge <nb_sites> <nb_edges> 
     * c
     * c The line "e x y" means that an edge exists between the nodes x and 
     * c y in the graph.
     * c
     * e <x> <y>
     * ```
     * 
     * @param text a text representation of the problem instance (in DIMACS edge format)
     * @return a parsed instance
     */
    public static MaximumDecarbonationInstance fromString(final String text) {
        final Pattern comment = Pattern.compile("^c\\s+");
        final Pattern header  = Pattern.compile("^p edge (?<nodes>\\d+)\\s+(?<edges>\\d+)\\s*$");
        final Pattern edge    = Pattern.compile("^e (?<from>\\d+)\\s+(?<to>\\d+)\\s*$");

        final Pin deref     = new Pin();

        for (String s : text.split("\n"))  {
            // discard any comment
            if (comment.matcher(s).matches()) {
                continue;
            }
            // process the header
            Matcher captures = header.matcher(s); 
            if (captures.matches()) {
                deref.nodes     = Integer.parseInt(captures.group("nodes"));
                deref.edges     = Integer.parseInt(captures.group("edges"));
                deref.processed = 0;
                deref.instance  = new MaximumDecarbonationInstance(deref.nodes);
                continue;
            }
            // process one edge
            captures = edge.matcher(s); 
            if (captures.matches()) {
                final int from  = Integer.parseInt(captures.group("from"));
                final int to    = Integer.parseInt(captures.group("to"));
                deref.processed+= 1;
                deref.instance.drawEdge(from, to);
                continue;
            }
        }

        // ensure the number of edge matches what was announced
        if (deref.processed != deref.edges) {
            throw new IllegalStateException(String.format("The instance announces %d edges but only %d have been processed", deref.edges, deref.processed));
        }
        // return the final result
        return deref.instance;
    }
    /**
     * Parses the input of a file and returns a corresponding instance.
     * 
     * The instances are structured as follows
     * ```
     * c any line starting with the character 'c' is a comment and can be ignored
     * c
     * c The header line "p edge x y" tells the basic info required to parse the 
     * c file. It states how many nodes and edges there are in the graph.
     * c (If you are aware, this is the standard DIMACS edge format).
     * c
     * c Then any subsequent line starts with an 'e' to indicate that it represents
     * c an edge of the graph (this is an edge list representation).
     * c
     * p edge <nb_sites> <nb_edges> 
     * c
     * c The line "e x y" means that an edge exists between the nodes x and 
     * c y in the graph.
     * c
     * e <x> <y>
     * ```
     * 
     * @param fname the pathname of a file con
     * @return a parsed instance
     */
    public static MaximumDecarbonationInstance fromFile(final String fname) throws IOException {
        final StringBuilder out = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(new File(fname)))) {
            in.lines().forEachOrdered( s -> out.append(s).append('\n') );
        }
        return fromString(out.toString());
    }

    /** 
     * This object is kind of pointless, it only gives us an convenient way 
     * to get an effectively final mutable reference to the data we need when
     * parsing an instance from text.
     */
    private static class Pin {
        /** the number of nodes in the instance we create */
        int nodes = 0;
        /** the number of edges in the instance we create */
        int edges = 0;
        /** the number of edges that have already been processed */
        int processed = 0;
        /** the instance we are parsing */
        MaximumDecarbonationInstance instance = null;
    }  
}
