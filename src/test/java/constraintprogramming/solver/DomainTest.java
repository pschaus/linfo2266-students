package constraintprogramming.solver;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import util.NotImplementedException;
import util.NotImplementedExceptionAssume;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;


@Grade(value = 10)
public class DomainTest {
    
    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
    public void testRemoveBelowUnChanged() {
        try {
            Domain d = new Domain(10);
            assertFalse(d.removeBelow(-2), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse(d.removeBelow(Integer.MIN_VALUE), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse(d.removeBelow(0), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(9, d.max());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
    public void testRemoveBelow1() {
        try {
            Domain d = new Domain(10);
            assertTrue(d.removeBelow(2), "The domain has changed");
            assertEquals(2, d.min());
            assertEquals(9, d.max());
            assertFalse(d.removeBelow(0), "The domain has not changed");
            assertEquals(2, d.min());
            assertEquals(9, d.max());
            assertTrue(d.removeBelow(7), "The domain has changed");
            assertEquals(7, d.min());
            assertEquals(9, d.max());
            assertTrue(d.removeBelow(9), "The domain has changed");
            assertEquals(9, d.min());
            assertEquals(9, d.max());
            assertTrue(d.isFixed());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
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

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
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

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
    public void testRemoveAboveUnchanged() {
        try {
            Domain d = new Domain(10);
            assertFalse(d.removeAbove(11), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse(d.removeAbove(Integer.MAX_VALUE), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(9, d.max());
            assertFalse(d.removeAbove(9), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(9, d.max());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
    public void testRemoveAbove1() {
        try {
            Domain d = new Domain(10);
            assertTrue(d.removeAbove(7), "The domain has changed");
            assertEquals(0, d.min());
            assertEquals(7, d.max());
            assertFalse(d.removeAbove(8), "The domain has not changed");
            assertEquals(0, d.min());
            assertEquals(7, d.max());
            assertTrue(d.removeAbove(3), "The domain has changed");
            assertEquals(0, d.min());
            assertEquals(3, d.max());
            assertTrue(d.removeAbove(0), "The domain has changed");
            assertEquals(0, d.min());
            assertEquals(0, d.max());
            assertTrue(d.isFixed());
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        } catch (TinyCSP.Inconsistency e) {
            fail("You said that there was an inconsistency although the operations were valid");
        }
    }

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
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

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
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

    @Test
    @Grade(cpuTimeout = 100, unit = MILLISECONDS)
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
