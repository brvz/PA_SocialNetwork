package Logger;

import smartgraph.view.graphview.SmartGraphProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerProperties {

    private static final boolean DEFAULT_USER_INCLUDED = true;
    private static final String PROPERTY_USER_INCLUDED = "user.user_included";

    private static final boolean DEFAULT_USER_ADDED = true;
    private static final String PROPERTY_USER_ADDED = "user.user_added";

    private static final boolean DEFAULT_RELATION_ADDED = true;
    private static final String PROPERTY_RELATION_ADDED = "relationship.relation_added";

    private static final boolean DEFAULT_UNDO = true;
    private static final String PROPERTY_UNDO = "graph.undo";

    private static final boolean DEFAULT_REDO = true;
    private static final String PROPERTY_REDO = "graph.redo";

    private static final String DEFAULT_FILE = "log.properties";
    private Properties properties;

    public LoggerProperties() {
        properties = new Properties();

        try {
            properties.load(new FileInputStream(DEFAULT_FILE));
        } catch (IOException ex) {
            String msg = String.format("The default %s was not found. Using default values.", DEFAULT_FILE);
            Logger.getLogger(SmartGraphProperties.class.getName()).log(Level.WARNING, msg);
        }
    }

    public LoggerProperties(InputStream inputStream) {
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException ex) {
            String msg = "The file provided by the input stream does not exist. Using default values.";
            Logger.getLogger(SmartGraphProperties.class.getName()).log(Level.WARNING, msg);
        }
    }

    public boolean getUserIncluded() {
        return getBooleanProperty(PROPERTY_USER_INCLUDED, DEFAULT_USER_INCLUDED);
    }


    public boolean getUserAdded() {
        return getBooleanProperty(PROPERTY_USER_ADDED, DEFAULT_USER_ADDED);
    }



    public boolean getRelationAdded() {
        return getBooleanProperty(PROPERTY_RELATION_ADDED, DEFAULT_RELATION_ADDED);
    }


    public boolean getUndo() {
        return getBooleanProperty(PROPERTY_UNDO, DEFAULT_UNDO);
    }

    public boolean getRedo() {
        return getBooleanProperty(PROPERTY_REDO, DEFAULT_REDO);
    }

    private boolean getBooleanProperty(String propertyName, boolean defaultValue) {
        String p = properties.getProperty(propertyName, Boolean.toString(defaultValue));
        try {
            return Boolean.valueOf(p);
        } catch (NumberFormatException e) {
            System.err.printf("Error in reading property %s: %s", propertyName, e.getMessage());
            return defaultValue;
        }
    }
}
