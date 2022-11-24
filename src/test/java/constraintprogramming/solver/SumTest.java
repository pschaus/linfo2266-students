package constraintprogramming.solver;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@GradeClass(totalValue = 30)
public class SumTest {

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testUpdateY() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable y = csp.makeVariable(100);
            Variable[] x = new Variable[]{
                    csp.makeVariable(5+1), // exclusive range, the +1 are for readability
                    csp.makeVariable(10+1),
                    csp.makeVariable(20+1),
            };
            csp.sum(x, y);
            assertEquals(0, y.dom.min());
            assertEquals("The maximum value of y should be changed according to x", 35, y.dom.max());
            assertEquals(0, x[0].dom.min());
            assertEquals(5, x[0].dom.max());
            assertEquals(0, x[1].dom.min());
            assertEquals(10, x[1].dom.max());
            assertEquals(0, x[2].dom.min());
            assertEquals(20, x[2].dom.max());
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the arguments are valid for a sum constraint");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testInconsistency() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable y = csp.makeVariable(100);
            y.dom.removeBelow(36);
            Variable[] x = new Variable[]{
                    csp.makeVariable(5+1), // exclusive range, the +1 are for readability
                    csp.makeVariable(10+1),
                    csp.makeVariable(20+1),
            };
            csp.sum(x, y);
            fail("Inconsistency should be detected by the fixpoint when adding the constraint");
        } catch (TinyCSP.Inconsistency ignored) {

        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testUpdateX() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable y = csp.makeVariable(10+1);
            Variable[] x = new Variable[]{
                    csp.makeVariable(5+1), // exclusive range, the +1 are for readability
                    csp.makeVariable(5+1),
                    csp.makeVariable(5+1),
            };
            y.dom.fix(10); // y == {10}
            x[0].dom.removeBelow(4); // x0 == {4, 5}
            x[1].dom.removeBelow(4); // x1 == {4, 5}
            csp.sum(x, y);
            // x2 == {0, 1, 2}
            assertEquals(4, x[0].dom.min());
            assertEquals(5, x[0].dom.max());
            assertEquals(4, x[1].dom.min());
            assertEquals(5, x[1].dom.max());
            assertEquals(0, x[2].dom.min());
            assertEquals("You should update the x's based on the other ones and based on y", 2, x[2].dom.max());
            assertEquals("You should update the x's based on the other ones and based on y", 3, x[2].dom.size());
        } catch (TinyCSP.Inconsistency ignored) {
            fail("You said that there was an inconsistency although the arguments are valid for a sum constraint");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testUpdateXAndY() {
        try {
            TinyCSP csp = new TinyCSP();
            Variable y = csp.makeVariable(20+1);
            Variable[] x = new Variable[]{
                    csp.makeVariable(5+1), // exclusive range, the +1 are for readability
                    csp.makeVariable(5+1),
                    csp.makeVariable(5+1),
            };
            x[0].dom.removeBelow(3); // x0 == {3, 4, 5}
            x[1].dom.removeBelow(2); // x1 == {2, 3, 4, 5}
            x[2].dom.fix(1); // x1 == {1}
            csp.sum(x, y);
            // y == {6, ..., 11}
            assertEquals("You need to change y according to x", 6, y.dom.min());
            assertEquals("You need to change y according to x", 11, y.dom.max());

            y.dom.removeBelow(8);
            csp.fixPoint();
            assertEquals(3, x[0].dom.min()); // unchanged: 3 + 4 + 1 == 8
            assertEquals(5, x[0].dom.max());
            assertEquals(2, x[1].dom.min()); // unchanged: 5 + 2 + 1 == 8
            assertEquals(5, x[1].dom.max());
            assertEquals(1, x[2].dom.value());

            y.dom.removeBelow(9); // y == {9, 10, 11}
            csp.fixPoint();
            // x1 == {3, 4, 5}
            assertEquals(3, x[0].dom.min()); // unchanged: 3 + 5 + 1 == 9
            assertEquals(5, x[0].dom.max());
            assertEquals(3, x[1].dom.min());
            assertEquals(5, x[1].dom.max());
            assertEquals(1, x[2].dom.value());

            y.dom.removeBelow(10); // y == {10, 11}
            csp.fixPoint();
            // x0 == {4, 5}
            // x1 == {4, 5}
            assertEquals(4, x[0].dom.min()); // unchanged: 3 + 4 + 1 == 8
            assertEquals(5, x[0].dom.max());
            assertEquals(4, x[1].dom.min()); // unchanged: 5 + 2 + 1 == 8
            assertEquals(5, x[1].dom.max());
            assertEquals(1, x[2].dom.value());
        } catch (TinyCSP.Inconsistency ignored) {
            fail("You said that there was an inconsistency although the arguments are valid for a sum constraint");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

}
