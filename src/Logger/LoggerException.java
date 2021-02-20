package Logger;

/**
 * LoggerException is a class that if a exception is activated in a method that receives it, return a string.
 */
public class LoggerException extends RuntimeException {
    public LoggerException(String message){
        super(message);
    }
}
