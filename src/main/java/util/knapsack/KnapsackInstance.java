package util.knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import dynamicprogramming.DynamicProgramming;
import dynamicprogramming.Knapsack;
import dynamicprogramming.KnapsackState;

public class KnapsackInstance {

    public int n, capacity;
    public int[] value, weight;
    public final int objective;

    public KnapsackInstance(int capacity, int[] value, int[] weight) {
        this.n = value.length;
        this.capacity = capacity;
        this.value = value;
        this.weight = weight;
        this.objective = -1;
    }

    public KnapsackInstance(int seed, int n, int capacity, int valueMean, int valueSpread, int weightMean, int weightSpread) {
        Random rand = new Random(seed);
        
        this.n = n;
        this.capacity = capacity;

        this.value = new int[n];
        this.weight = new int[n];

        for (int i = 0; i < n; i++) {
            this.value[i] = valueMean + rand.nextInt(valueSpread + 1)  - valueSpread / 2;
            this.weight[i] = weightMean + rand.nextInt(weightSpread + 1) - weightSpread / 2;
        }

        this.objective = -1;
    }

    public KnapsackInstance(String path) {
        File file = new File(path);
        Scanner scanner = null;
        int obj = -1;
        
        try {
            scanner = new Scanner(file);

            this.n = scanner.nextInt();
            this.capacity = scanner.nextInt();
            obj = scanner.nextInt();

            this.value = new int[n];
            this.weight = new int[n];

            for (int i = 0; i < n; i++) {
                value[i] = scanner.nextInt();
                weight[i] = scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        this.objective = obj;
    }

    public void save(String path) {
        Knapsack model = new Knapsack(this);
        DynamicProgramming<KnapsackState> solver = new DynamicProgramming<>(model);
        int objective = (int) solver.getSolution().getValue();

        FileWriter fileWriter = null;
        PrintWriter printWriter = null;

        try {
            fileWriter = new FileWriter(path);
            printWriter = new PrintWriter(fileWriter);

            printWriter.printf("%d %d %d\n", n, capacity, objective);

            for (int i = 0; i < n; i++) {
                printWriter.printf("%d %d\n", value[i], weight[i]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public static void generate(int seed, int n, int capacity, int valueMean, int valueSpread, int weightMean, int weightSpread) {
        KnapsackInstance instance = new KnapsackInstance(seed, n, capacity, valueMean, valueSpread, weightMean, weightSpread);
        instance.save("data/Knapsack/instance_n" + n + "_c" + capacity + "_" + valueMean + "_" + valueSpread + "_" + weightMean + "_" + weightSpread + "_" + seed);
    }

    public static void main(String[] args) {
        for (int seed = 0; seed < 10; seed++) {
            generate(seed, 100, 500, 10, 5, 100, 5);
        }
    }

}
