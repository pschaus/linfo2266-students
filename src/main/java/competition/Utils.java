package competition;

public class Utils {
    /**
     * Associates a unique integer to each possible pair of nodes,
     * the order of the nodes is not important : hashPair(a,b,n) = hashPair(b,a,n)
     * @param a node A
     * @param b node B
     * @param n number of nodes in the topology
     * @return the hash of nodeA and nodeB
     */
    public static int hashPair(int a, int b, int n) {
        if (a >= n || b >= n) {
            throw new IllegalArgumentException("value in pairs cannot be more or equal than n");
        }
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("a and b cannot be negative");
        }
        if (a == b) {
            throw new IllegalArgumentException("a and b cannot be equals");
        }
        if (a < b) {
            return (n - 1) * (n - 2) / 2 - (n - a - 1) * (n - a - 2) / 2 + b - 1;
        }
        else {
            return (n - 1) * (n - 2) / 2 - (n - b - 1) * (n - b - 2) / 2 + a - 1;
        }
    }

}
