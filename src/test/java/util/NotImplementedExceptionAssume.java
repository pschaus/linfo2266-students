package util;

import org.junit.Assume;

public class NotImplementedExceptionAssume {
    public static void fail(NotImplementedException e) {
        Assume.assumeNoException(e);
    }
}
