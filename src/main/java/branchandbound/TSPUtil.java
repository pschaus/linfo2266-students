package branchandbound;

import util.UF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TSPUtil {

    public static boolean isOneTree(int n, Iterable<Edge> edges) {
        UF uf = new UF(n);
        int size = 0;
        Set<Integer> adjacent0 = new HashSet<>();
        // only two components when not connecting node 0
        for (Edge e: edges) {
            if (e.v1() != 0 && e.v2() != 0) {
                uf.union(e.v1(),e.v2());
            } else {
                adjacent0.add(e.v1() == 0 ? e.v2() : e.v1());
            }
            size++;
        }
        if (adjacent0.size() != 2) return false;
        if (n != size) return false;
        if (2 != uf.count()) return false;
        return true;
    }

    public static boolean circuitExist(boolean[][] excluded) {
        // test that there is at least a one-tree possible
        // this test could be improved by checking that the graph is strongly connected
        int n = excluded.length;
        UF uf = new UF(n);
        for (int i = 1; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (!excluded[i][j]) {
                    uf.union(i,j);
                }
            }
        }
        // check that the exclusion of node 0 does not disconnect the graph
        if (uf.count() > 2) return false;
        // check at least two edges are adjacent to node 0
        int i = 0;
        for (int j = 1; j < n; j++) {
            if (!excluded[0][j]) i++;
        }
        return i >= 2;
    }


    /**
     * Compute all the possible subset of a set of a given size
     * @param originalSet
     * @param size
     * @return the set of all subsets of the original set of the given size
     * @param <T>
     */
    public static <T> Set<Set<T>> powerSet(Set<T> originalSet, int size) {
        Set<Set<T>> sets = new HashSet<>();
        if (size == 0) {
            sets.add(new HashSet<>()); // Add the empty set if size 0 is requested
            return sets;
        }
        if (originalSet.isEmpty() || size < 0) {
            return sets; // Return an empty set if the size is negative or the set is empty
        }

        List<T> list = new ArrayList<>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<>(list.subList(1, list.size()));

        // Recursively get subsets of the rest of the elements
        for (Set<T> set : powerSet(rest, size - 1)) {
            Set<T> newSet = new HashSet<>(set);
            newSet.add(head);
            if (newSet.size() == size) {
                sets.add(newSet);
            }
        }
        // Add the subsets from rest that do not include the head
        for (Set<T> set : powerSet(rest, size)) {
            if (set.size() == size) {
                sets.add(set);
            }
        }

        return sets;
    }
}
