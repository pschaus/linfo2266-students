package constraintprogramming.solver;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeClass;
import org.junit.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import static org.junit.Assert.*;

@GradeClass(totalValue = 10)
public class DomainTest {

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveBelowUnChanged() {
        try {
            Domain d = new Domain(10);
            assertFalse("The domain has not changed", d.removeBelow(-2));
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse("The domain has not changed", d.removeBelow(Integer.MIN_VALUE));
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse("The domain has not changed", d.removeBelow(0));
            assertEquals(0, d.min());
            assertEquals(9, d.max());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveBelow1() {
        try {
            Domain d = new Domain(10);
            assertTrue("The domain has changed", d.removeBelow(2));
            assertEquals(2, d.min());
            assertEquals(9, d.max());
            assertFalse("The domain has not changed", d.removeBelow(0));
            assertEquals(2, d.min());
            assertEquals(9, d.max());
            assertTrue("The domain has changed", d.removeBelow(7));
            assertEquals(7, d.min());
            assertEquals(9, d.max());
            assertTrue("The domain has changed", d.removeBelow(9));
            assertEquals(9, d.min());
            assertEquals(9, d.max());
            assertTrue(d.isFixed());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveBelowFailure1() {
        Domain d = new Domain(10);
        try {
            d.removeBelow(11);
            fail("You need to throw an inconsistency if the domains becomes empty");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {

        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveBelowFailure2() {
        Domain d = new Domain(10);
        try {
            d.removeBelow(Integer.MAX_VALUE / 2);
            fail("You need to throw an inconsistency if the domains becomes empty");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {

        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveAboveUnchanged() {
        try {
            Domain d = new Domain(10);
            assertFalse("The domain has not changed", d.removeAbove(11));
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse("The domain has not changed", d.removeAbove(Integer.MAX_VALUE));
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse("The domain has not changed", d.removeAbove(9));
            assertEquals(0, d.min());
            assertEquals(9, d.max());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveAbove1() {
        try {
            Domain d = new Domain(10);
            assertTrue("The domain has changed", d.removeAbove(7));
            assertEquals(0, d.min());
            assertEquals(7, d.max());
            assertFalse("The domain has not changed", d.removeAbove(8));
            assertEquals(0, d.min());
            assertEquals(7, d.max());
            assertTrue("The domain has changed", d.removeAbove(3));
            assertEquals(0, d.min());
            assertEquals(3, d.max());
            assertTrue("The domain has changed", d.removeAbove(0));
            assertEquals(0, d.min());
            assertEquals(0, d.max());
            assertTrue(d.isFixed());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveAboveFailure1() {
        Domain d = new Domain(10);
        try {
            d.removeAbove(-10);
            fail("You need to throw an inconsistency if the domains becomes empty");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {

        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveAboveFailure2() {
        Domain d = new Domain(10);
        try {
            d.removeAbove(-1);
            fail("You need to throw an inconsistency if the domains becomes empty");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {

        }
    }

    @Test(timeout = 300)
    @Grade(cpuTimeout = 100)
    public void testRemoveAboveFailure3() {
        Domain d = new Domain(10);
        try {
            d.removeAbove(Integer.MIN_VALUE / 2);
            fail("You need to throw an inconsistency if the domains becomes empty");
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {

        }
    }


}
