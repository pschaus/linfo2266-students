package branchandbound;

import util.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation of the Knapsack solved using the {@link BranchAndBound}
 */
public class BranchAndBoundKnapsack {
    public static void main(String[] args) {

        int[] value = new int[]{1, 6, 18, 22, 28};
        int[] weight = new int[]{2, 3, 5, 6, 7};
        int capa = 11;
        int n = value.length;


        String instance = "data/knapsack/knapsackA";
        InputReader reader = new InputReader(instance);
        n = reader.getInt();
        capa = reader.getInt();
        value = new int[n];
        weight = new int[n];
        for (int i = 0; i < n; i++) {
            value[i] = reader.getInt();
            weight[i] = reader.getInt();
        }
        // sort decreasing according to value/weight ratio

        sortValueOverWeight(value,weight);
        for (int i = 0; i < n-1; i++) {
            assert ((double) value[i]/weight[i] > (double) value[i-1]/weight[i+1]);
        }




        OpenNodes<NodeKnapsack> openNodes = new BestFirstOpenNodes<>();
        //OpenNodes<NodeKnapsack> openNodes = new DepthFirstOpenNodes<>();

        NodeKnapsack root = new NodeKnapsack(null,value,weight,0,capa,-1,false);
        openNodes.add(root);

        BranchAndBound.minimize(openNodes,node -> {
            System.out.println("new best solution: "+- node.lowerBound());
        });


    }

    private static void sortValueOverWeight(int [] value, int [] weight) {
        int n = value.length;
        double [][] item = new double[n][2];
        for (int i = 0; i < n; i++) {
            item[i][0] = value[i];
            item[i][1] = weight[i];
        }
        Arrays.sort(item,(i1,i2)-> (i1[0]/i1[1] > i2[0]/i2[1]) ? -1 : 1 );
        for (int i = 0; i < n; i++) {
            value[i] = (int) item[i][0];
            weight[i] = (int) item[i][1];
        }
    }
}

/**
 * Node/State implementation of the Knapsack
 * In this representation, a node is related
 * to the decision at of item positioned at index
 * with value[index] and weight[index].
 * Depending on the `selected` status, the item
 * is considered to be part of the knapsack
 * of excluded of it.
 * In this representation, all the items before
 * index have already been decided and the ones after
 * are undecided and remain to be branched on.
 */
class NodeKnapsack implements Node<NodeKnapsack> {

    int[] value;
    int[] weight;
    int selectedValue;
    int capaLeft;
    int index;
    boolean selected;
    NodeKnapsack parent;
    double ub;

    int depth;

    /**
     *
     * @param parent reference to the parent node
     * @param value values of each item of the problem
     * @param weight weights of each item of the problem
     * @param selectedValue total amount of value in the knapsack
     * @param capaLeft total amount of capacithy left in the knapsack
     * @param index the index of item related to the decision of this node
     * @param selected is the item at index included or not in the knapsack
     */
    public NodeKnapsack(NodeKnapsack parent, int[] value, int[] weight,
                        int selectedValue, int capaLeft, int index, boolean selected) {
        this.parent = parent;
        this.value = value;
        this.weight = weight;
        this.selectedValue = selectedValue;
        this.capaLeft = capaLeft;
        this.index = index;
        this.selected = selected;
        this.depth = parent == null ? 0: parent.depth+1;
        this.ub = lpRelaxUBound();
        //this.ub = capacityRelaxUBound();
    }

    @Override
    public int depth() {
        return depth;
    }

    /**
     * Computes an upper-bound obtained
     * by relaxing the capacity constraint
     */
    private double capacityRelaxUBound() {
        int valueUb = selectedValue;
        for (int i = index + 1; i < value.length; i++) {
            valueUb += value[i];
        }
        return valueUb; // maximization problem
    }

    /**
     * Computes an upper-bound obtained
     * with linear programming relaxation
     * that is each item yet to be decided
     * can be fractionally selected.
     */
    private double lpRelaxUBound() {
        int valueUb = selectedValue;
        int c = capaLeft;
        for (int i = index + 1; i < value.length; i++) {
            if (weight[i] < c) {
                valueUb += value[i];
                c -= weight[i];
            } else {
                valueUb += ((double) c)/weight[i] * value[i];
                break;
            }
        }
        return valueUb; // maximization problem
    }

    @Override
    public double objectiveFunction() {
        return -ub;
    }

    @Override
    public double lowerBound() {
        return -ub;
    }

        @Override
        public boolean isSolutionCandidate() {
            return index == value.length - 1;
        }

    @Override
    public List<Node<NodeKnapsack>> children() {

        List<Node<NodeKnapsack>> children = new ArrayList<>();
        // do not select item at index+1
        NodeKnapsack right = new NodeKnapsack(this, value, weight,
                selectedValue,
                capaLeft,
                index + 1, false);
        children.add(right);
        if (capaLeft >= weight[index+1]) {
            // select item at index+1
            NodeKnapsack left = new NodeKnapsack(this, value, weight,
                    selectedValue + value[index + 1],
                    capaLeft - weight[index + 1],
                    index + 1, true);
            children.add(left);
        }
        return children;
    }

    @Override
    public String toString() {
        ArrayList<Integer> selected = new ArrayList<>();
        NodeKnapsack current = this;
        while (current.index != -1) {
            if (current.selected) {
                selected.add(current.index);
            }
            current = current.parent;
        }
        return selected.toString();
    }

    @Override
    public NodeKnapsack getState() {
        return this;
    }
}


