package model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * Singleton class responsible for the logger operations. This class will create a file logger.txt and feature some
 * data insertions with their respective date stamps.
 */

public final class Logger {
    private static Logger instance = null;
    private static final String LOGGERFILE = "log.txt";
    private PrintStream printStream;
    private final LoggerProperties loggerProperties;

    public Logger(LoggerProperties properties){
        connect();
        this.loggerProperties = properties != null ? properties : new LoggerProperties();
    }

    public Logger(){
        this(null);
    }

    private boolean connect(){
        if(printStream == null) {
            try {
                printStream = new PrintStream(new FileOutputStream(LOGGERFILE), true);
            } catch (FileNotFoundException ex) {
                printStream = null;
                return false;
            }

            return true;
        }

        return true;
    }

    public void writeToFile(String str) { printStream.println(new Date().toString() + " " + str); }

    public static Logger getInstance() {
        if (instance == null)
            instance = new Logger();

        return instance;
    }

}
