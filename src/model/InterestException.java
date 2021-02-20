package model;

/**
 * InterestException is a class that if a exception is activated in a method that receives it, return a string.
 */
public class InterestException extends RuntimeException{
    public InterestException(String string) {
        super(string);
    }
}
