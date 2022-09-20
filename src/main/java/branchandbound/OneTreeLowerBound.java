package branchandbound;

import util.UF;

import java.util.List;




public abstract class OneTreeLowerBound {


    public abstract  OneTreeResult compute(double [][] distanceMatrix, boolean[][] excluded);

    public boolean oneTreeExist(boolean[][] excluded) {
        int n = excluded.length;
        UF uf = new UF(n);
        for (int i = 1; i < n; i++) {
            for (int j = i+i; j < n; j++) {
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

}
