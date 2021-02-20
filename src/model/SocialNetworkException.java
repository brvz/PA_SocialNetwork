package model;

/**
 * SocialNetworkException is a class that if a exception is activated in a method that receives it, return a string.
 */
public class SocialNetworkException extends RuntimeException {
    public SocialNetworkException(String string) {
        super(string);
    }
}
