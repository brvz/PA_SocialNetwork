package model;

/**
 * Exception class responsible for casting a string when an exception is thrown.
 */
public class DefaultException extends RuntimeException {
    public DefaultException(String string) {
        super(string);
    }
}
