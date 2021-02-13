package Logger;


import com.pa.proj2020.adts.graph.Graph;
import smartgraph.view.graphview.SmartGraphProperties;
import smartgraph.view.graphview.SmartPlacementStrategy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

public class Logger  {
    private static final String LogFile = "log.txt";
    private PrintStream printStream;

    /*
    CONFIGURATION PROPERTIES
     */
    private final LoggerProperties loggerProperties;

    public Logger(LoggerProperties properties){
        connect();
        this.loggerProperties = properties != null ? properties : new LoggerProperties();
    }
    public Logger(){
        this(null);
    }

    private boolean connect(){
        if(printStream == null){
            try {
                printStream = new PrintStream(new FileOutputStream(LogFile), true);
            }catch (FileNotFoundException ex){
                printStream = null;
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * Writes to a file (logs) with useful information
     * about the social network
     *
     * @param str - useful info about the users and relationships in
     *the social network
     * @throws LoggerException
     */
    public void writeToFile(String str) throws LoggerException{
        if(printStream == null){
            throw new LoggerException("Failed to write on file!");
        }
        printStream.println(new Date().toString() + " " + str);
    }

}
