package util;

public class NotImplementedException extends UnsupportedOperationException {
    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException() {
        super();
    }

    public void print() {
        System.err.println(this + " " + (getStackTrace()[0].toString()));
    }

}