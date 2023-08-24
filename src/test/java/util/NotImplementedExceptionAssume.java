package util;

import org.junit.jupiter.api.Assumptions;

public class NotImplementedExceptionAssume {
    public static void fail(NotImplementedException e) {
        Assumptions.abort(e.getMessage());
    }
}
