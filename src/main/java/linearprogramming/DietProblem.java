package linearprogramming;

import util.InputReader;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Solves a diet problem
 */
public class DietProblem {

    public final double[][] A;
    public final double[] b;
    public final double[] c;

    // all nutrients present in the instances
    private final static String[] nutrients = new String[] {
            "calories",
            "protein",
            "fat",
            "sodium",
    };

    // map of nutrient -> index
    private final static Map<String, Integer> nutrientsMap = IntStream.range(0, nutrients.length).boxed().collect(Collectors.toMap(i -> nutrients[i], i -> i));

    // an entry related to food
    public static class FoodEntry {
        private String name;
        private double cost;
        private double[] nutrientsValues;

        private FoodEntry(String name, double cost, double[] nutrientsValues) {
            this.name = name;
            this.cost = cost;
            this.nutrientsValues = nutrientsValues;
        }

        // gives the value associated to a nutrient
        private double value(String nutrient) {
            return nutrientsValues[nutrientsMap.get(nutrient)];
        }

    }

    /**
     * Reads a file where food entries are written, and creates Java objects representing the entries for the food
     * @param filename path where the food entries are written
     * @return food entries
     */
    public static FoodEntry[] readFoodFile(String filename) {
        InputReader reader = new InputReader(filename);
        FoodEntry[] food = new FoodEntry[reader.getInt()]; // first line contains the number of nutrients
        // second line must be ignored
        for (int i = 0 ; i < nutrients.length+2 ; i++)
            reader.getString();
        for (int i = 0 ; i < food.length ; i++) {
            String name = reader.getString();
            double cost = reader.getDouble();
            double[] nutrientsValues = new double[nutrients.length];
            for (int j = 0 ; j < nutrientsValues.length ; j++)
                nutrientsValues[j] = reader.getDouble();
            food[i] = new FoodEntry(name, cost, nutrientsValues);
        }
        return food;
    }

    /**
     * Reads a file where profile requirement are written, and creates a Java object representing the food requirements
     * @param filename path where the food requirements are written
     * @return food requirements
     */
    public static Requirement readRequirementFile(String filename) {
        InputReader reader = new InputReader(filename);
        // first line must be ignored
        for (int i = 0 ; i < 3 ; i++)
            reader.getString();
        double[] minRequirement = new double[nutrients.length];
        double[] maxRequirement = new double[nutrients.length];
        for (int i = 0 ; i < nutrients.length ; i++) {
            String nutrient = reader.getString();
            minRequirement[nutrientsMap.get(nutrient)] = reader.getDouble();
            maxRequirement[nutrientsMap.get(nutrient)] = reader.getDouble();
        }
        return new Requirement(minRequirement, maxRequirement);
    }

    // profile for food requirement
    public static class Requirement {
        private double[] minRequirement; // minimum amount for the nutrient
        private double[] maxRequirement; // maximum amount for the nutrient

        public Requirement(double[] minRequirement, double[] maxRequirement) {
            this.minRequirement = minRequirement;
            this.maxRequirement = maxRequirement;
        }

        public double getMinValue(String nutrient) {
            return minRequirement[nutrientsMap.get(nutrient)];
        }

        public double getMaxValue(String nutrient) {
            return maxRequirement[nutrientsMap.get(nutrient)];
        }
    }

    public DietProblem(FoodEntry[] foodEntries, Requirement requirement) {
        // one variable per food entry: how many should be taken?
        int nCols = foodEntries.length;
        // two constraints per nutrient: one for its minimum threshold, one for its maximum threshold
        int nRows = nutrients.length * 2;
        A = new double[nRows][nCols];
        b = new double[nRows];
        c = new double[nCols];
        // minimum threshold for each nutrient
        for (int i = 0 ; i < nutrients.length ; i++) {
            String nutrient = nutrients[i];
            // sum of aliments   >= minimum value
            // - sum of aliments <= - minimum value
            for (int j = 0 ; j < foodEntries.length ; j++)
                A[i][j] = - foodEntries[j].value(nutrient);
            b[i] = - requirement.getMinValue(nutrient);
        }
        int n = nutrients.length;
        // maximum threshold for each nutrient
        for (int i = 0 ; i < nutrients.length ; i++) {
            String nutrient = nutrients[i];
            // sum of aliments   <= maximum value
            for (int j = 0 ; j < foodEntries.length ; j++)
                A[n+i][j] = foodEntries[j].value(nutrient);
            b[n+i] = requirement.getMaxValue(nutrient);
        }
        // objective is to minimize the sum of cost per aliment
        for (int i = 0 ; i < foodEntries.length ; i++)
            c[i] = - foodEntries[i].cost;
    }

    public static void main(String[] args) {
        String foodFilename = "data/diet/tiny_food_1.txt";
        String requirementFilename = "data/diet/profile_1.txt";
        FoodEntry[] foodEntries = readFoodFile(foodFilename);
        Requirement requirement = readRequirementFile(requirementFilename);
        DietProblem problem = new DietProblem(foodEntries, requirement);
        TwoPhaseSimplex simplex1 = new TwoPhaseSimplex(problem.A, problem.b, problem.c);
        System.out.printf("cost 2 phase = %.2f$%n", - simplex1.value()); // cost is negated in the formulation
        BigMSimplex simplex2 = new BigMSimplex(problem.A, problem.b, problem.c, 1e5);
        System.out.printf("cost big M   = %.2f$%n", - simplex2.value()); // cost is negated in the formulation
    }

}
