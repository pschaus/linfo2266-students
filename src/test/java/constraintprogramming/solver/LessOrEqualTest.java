package constraintprogramming.solver;

import org.javagrader.Grade;
import org.javagrader.GradeFeedback;
import org.junit.jupiter.api.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import java.util.ArrayList;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.javagrader.TestResultStatus.FAIL;
import static org.junit.jupiter.api.Assertions.*;

@Grade(value = 10)
public class LessOrEqualTest {

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
    public void simpleTest() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable x = csp.makeVariable(16); // 0..15
            Variable y = csp.makeVariable(21); // 0..20
            x.dom.removeBelow(5);
            // x = 5..15

            csp.lessOrEqual(x, y);

            // x = 5..15
            // y = 5..20
            assertEquals(5, y.dom.min());

            y.dom.removeAbove(13);
            csp.fixPoint();
            // x = 5..13
            // y = 5..13
            assertEquals(9, x.dom.size());
            assertEquals(13, x.dom.max());

            x.dom.removeBelow(6);
            csp.fixPoint();

            // x = 6..13
            // y = 6..13
            assertEquals(6, y.dom.min());


        } catch (TinyCSP.Inconsistency e) {
            fail("should not fail");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
    @GradeFeedback(message = "You should only use removeAbove and removeBelow operations", on = FAIL)
    public void removeBoundTest() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable x = csp.makeVariable(11); // 0..10
            Variable y = csp.makeVariable(11); // 0..10

            x.dom.remove(1);
            x.dom.remove(2);
            x.dom.remove(3);
            // x = 1,4,5,6,7,8,9,10

            csp.lessOrEqual(x, y);

            assertEquals(0, y.dom.min());
            assertEquals(10, y.dom.max());
            assertEquals(11, y.dom.size());

            x.dom.remove(0);
            csp.fixPoint();

            // y = 4,5,6,7,8,9,10
            // y = 4,5,6,7,8,9,10
            assertEquals(4, y.dom.min());
            assertEquals(10, y.dom.max());
            assertEquals(7, y.dom.size());

            x.dom.removeAbove(6);
            csp.fixPoint();

            // x = 4,5,6
            // y = 4,5,6,7,8,9,10
            assertEquals(4, y.dom.min());
            assertEquals(10, y.dom.max());
            assertEquals(7, y.dom.size());
            assertEquals(4, x.dom.min());
            assertEquals(6, x.dom.max());
            assertEquals(3, x.dom.size());

            y.dom.removeAbove(5);
            csp.fixPoint();

            // x = 4,5
            // y = 4,5
            assertEquals(4, y.dom.min());
            assertEquals(5, y.dom.max());
            assertEquals(2, y.dom.size());
            assertEquals(4, x.dom.min());
            assertEquals(5, x.dom.max());
            assertEquals(2, x.dom.size());


        } catch (TinyCSP.Inconsistency e) {
            fail("should not fail");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(cpuTimeout = 300, unit = MILLISECONDS)
    public void searchTest1() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable x = csp.makeVariable(16); // 0..15
            Variable y = csp.makeVariable(21); // 0..20
            x.dom.removeBelow(5);
            // x = 5..15
            ArrayList<String> solutions = new ArrayList<>();
            for (int i = 5 ; i <= 15 ; i++)
                for (int j = 0 ; j <= 20 ; j++)
                    if (i <= j)
                        solutions.add(String.format("x= " + i + " y= " + j));
            csp.lessOrEqual(x, y);
            csp.dfs(() -> {
                assertTrue(x.dom.value() <= y.dom.value());
                String sol = String.format("x= " + x.dom.value() + " y= " + y.dom.value());
                assertTrue(solutions.contains(sol), "You found an invalid solution");
                solutions.remove(sol);
            });
            assertEquals(0, solutions.size(), "You did not find all solutions");
        } catch (TinyCSP.Inconsistency e) {
            fail("should not fail");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(cpuTimeout = 300, unit = MILLISECONDS)
    public void searchTest2() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable x = csp.makeVariable(21); // 0..20
            Variable y = csp.makeVariable(16); // 0..15
            y.dom.removeBelow(5);
            // y = 5..15
            ArrayList<String> solutions = new ArrayList<>();
            for (int i = 0 ; i <= 20 ; i++)
                for (int j = 5 ; j <= 15 ; j++)
                    if (i <= j)
                        solutions.add(String.format("x= " + i + " y= " + j));
            csp.lessOrEqual(x, y);
            csp.dfs(() -> {
                assertTrue(x.dom.value() <= y.dom.value());
                String sol = String.format("x= " + x.dom.value() + " y= " + y.dom.value());
                assertTrue(solutions.contains(sol), "You found an invalid solution");
                solutions.remove(sol);
            });
            assertEquals(0, solutions.size(), "You did not find all solutions");
        } catch (TinyCSP.Inconsistency e) {
            fail("should not fail");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test
    @Grade(cpuTimeout = 300, unit = MILLISECONDS)
    public void searchTest3() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable x = csp.makeVariable(7); // 0..6
            Variable y = csp.makeVariable(7); // 0..6
            for (int i = 0 ; i < 7 ; i+= 2) {
                x.dom.remove(i);
            }

            csp.lessOrEqual(x, y);
            ArrayList<String> solutions = new ArrayList<>();
            for (int i : new int[] {1, 3, 5})
                for (int j = 0 ; j < 7 ; j++)
                    if (i <= j)
                        solutions.add(String.format("x= " + i + " y= " + j));
            csp.dfs(() -> {
                assertTrue(x.dom.value() <= y.dom.value());
                String sol = String.format("x= " + x.dom.value() + " y= " + y.dom.value());
                assertTrue(solutions.contains(sol), "You found an invalid solution");
                solutions.remove(sol);
            });
            assertEquals(0, solutions.size(), "You did not find all solutions");
        } catch (TinyCSP.Inconsistency e) {
            fail("should not fail");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }
    
}
