package linearprogramming;

import org.javagrader.Grade;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static linearprogramming.DietProblem.readFoodFile;
import static linearprogramming.DietProblem.readRequirementFile;
import static org.junit.jupiter.api.Assertions.*;

@Grade
public class BigMSimplexTest {

    private BigMSimplex assertAtLeastNCalls(DietProblem problem, double bigM, int nCalls) {
        int m = problem.b.length;
        int n = problem.c.length;
        AtomicInteger calls = new AtomicInteger(0);
        AtomicReference<Double> objective = new AtomicReference<>(0.0);
        BigMSimplex bigMSimplex = new BigMSimplex(problem.A, problem.b, problem.c, bigM, (a) -> {
            // check the dimensions of the matrix
            assertEquals(m+1, a.length);
            assertEquals(n+m+m+1, a[0].length);
            int call = calls.getAndIncrement(); // increment the counter
            if (call == 0) {
                double obj = -a[m][n+m+m];
                objective.set(obj);
                // the BigM constant should appear somewhere in the matrix
                int nFound = 0;
                int nFoundExpected = 3;
                for (double[] row: a) {
                    for (double value: row) {
                        if (value == bigM) {
                            nFound++;
                            if (nFound == nFoundExpected)
                                break;
                        }
                    }
                    if (nFound == nFoundExpected)
                        break;
                }
                assertTrue(nFound >= nFoundExpected, "The BigM constant does not appear in your matrix");
            } else {
                double oldObj = objective.get();
                double newObj = -a[m][n+m+m];
                assertTrue(newObj >= oldObj, "The objective must always increase when using the simplex");
                objective.set(newObj);
            }
            // checks the objective value
        });
        assertTrue(calls.get() >= nCalls, "Are you pivoting at all?");
        assertEquals(objective.get(), bigMSimplex.value());
        return bigMSimplex;
    }

    @ParameterizedTest(name = "{0} and {1}, M={2}")
    @CsvSource({
            "data/diet/tiny_food_1.txt,data/diet/profile_1.txt,1e6,5,true",
            "data/diet/tiny_food_1.txt,data/diet/profile_1.txt,0,1,false",
            "data/diet/tiny_food_1.txt,data/diet/profile_1.txt,1e20,5,false",
            "data/diet/tiny_food_1.txt,data/diet/profile_2.txt,1e6,5,true",
            "data/diet/tiny_food_1.txt,data/diet/profile_2.txt,1e-3,5,false",
            "data/diet/tiny_food_1.txt,data/diet/profile_2.txt,1e20,5,false",
            "data/diet/tiny_food_2.txt,data/diet/profile_1.txt,1e5,5,true",
            "data/diet/tiny_food_2.txt,data/diet/profile_1.txt,1e-5,1,false",
            "data/diet/tiny_food_2.txt,data/diet/profile_1.txt,1e20,5,false",
            "data/diet/tiny_food_2.txt,data/diet/profile_2.txt,1e6,5,true",
            "data/diet/tiny_food_2.txt,data/diet/profile_2.txt,1e-3,5,false",
            "data/diet/tiny_food_2.txt,data/diet/profile_2.txt,1e20,5,false",
    })
    @Grade
    public void testDietProblem1(String foodFilename, String requirementFilename, double bigM, int nCalls, boolean equal) {
        DietProblem.FoodEntry[] foodEntries = readFoodFile(foodFilename);
        DietProblem.Requirement requirement = readRequirementFile(requirementFilename);
        DietProblem problem = new DietProblem(foodEntries, requirement);
        TwoPhaseSimplex twoPhase = new TwoPhaseSimplex(problem.A, problem.b, problem.c);
        double costTwoPhase = - twoPhase.value(); // cost is negated in the formulation
        BigMSimplex bigMSimplex = assertAtLeastNCalls(problem, bigM, nCalls);
        double costBigM = - bigMSimplex.value(); // cost is negated in the formulation
        if (equal)
            assertEquals(costBigM, costTwoPhase, 1e-5);
        else
            assertNotEquals(costBigM, costTwoPhase, 1e-5);
    }

}
