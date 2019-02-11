package VarexpInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/*
This class is responsible for interfacing with the config file config.xml and loading them up into member variables that
can then be used statically

All it does is return the default parameters to other classes that needs them

This Manager class really should be executed first before any other classes
 */
public class PropertyManager {

    //Log
    private static final Logger log = LogManager.getLogger(PropertyManager.class);

    //Member variables. These are static so I don't need to freaking instaneate an object before using. That sounds stupid
    private static String user;
    private static String password;
    private static String databaseIP;
    private static String defaultFileName;
    private static String defaultFilePath;
    private static String currentMachine;
    private static String propertiesFileName = "properties.xml";
    private static String logFileSize;

    InputStream inputStream;

    //Accessors
    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDatabaseIP() {
        return databaseIP;
    }

    public static String getDefaultFileName() {
        return defaultFileName;
    }

    public static String getDefaultFilePath() {
        return defaultFilePath;
    }

    public static String getLogFileSize() {
        return logFileSize;
    }

    private static File getFile(String filePath, String fileName) {
        log.debug("In getFile. Returning " + filePath + fileName);
        return new File(filePath + fileName);
    }

    public void getPropertyValues() throws IOException {
        //String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String rootPath = new File(".").getCanonicalPath() + "\\";
        System.out.println(rootPath);
        String propertiesConfigPath = rootPath + propertiesFileName;
        try {

            log.info("Loading default settings from configuration.xml");
            //Load from XML. If it isn't available, create it I guess"
            Properties prop = new Properties();
            prop.loadFromXML(new FileInputStream(propertiesConfigPath));

            //Set to member variables
            user = prop.getProperty("user");
            databaseIP = prop.getProperty("databaseIP");
            password = prop.getProperty("password");
            defaultFileName = prop.getProperty("defaultFileName");
            logFileSize = prop.getProperty("LogSize(MB)");
            //defaultFilePath = prop.getProperty("defaultFilePath");

            //Get the path on where the java app was executed
            defaultFilePath = prop.getProperty("defaultFilePath");

            //Get the Current Hostname. This is needed as you might not be querying data from localhost
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            currentMachine = localMachine.toString();

        }
        //If the properties file doesn't exist, then create it
        catch (Exception e) {
            System.out.println("Exception: " + e);
            log.info("No Configuration XML found, creating one now");
            createPropertiesXML(rootPath);
            log.info("Finsiehd creating default configuration");

        }
    }

    //Create a XML. SHould only be used if XML doesn't exist.
    // While you're at it, set the variables
    private void createPropertiesXML(String rootPath) {
        try {
            Properties props = new Properties();

            props.setProperty("user", "production");
            props.setProperty("databaseIP", "localhost");
            props.setProperty("password", "ZAQ!xsw2CDE#");
            props.setProperty("defaultFileName", "output.csv");
            props.setProperty("LogSize(MB)", "100");
            // save the CSV file to the same place where the JAR is executed
            props.setProperty("defaultFilePath", rootPath);

            //Where to store the properties file
            OutputStream os = new FileOutputStream(rootPath + propertiesFileName);

            //Store the properties detail into a pre-defiend XML file
            props.storeToXML(os, "Varexp Comparison Properties", "UTF-8");
            System.out.println("Done");

        } catch (Exception e) {
            log.error("Error. Something unusual happened when creating the default property. " +
                    "See error: ", e);
        }
    }

    //Checks the log file size and delete if needed
    public void logFileSizeCheck() {
        File file = null;
        //File file = getFile();
        boolean fileDeleted = file.delete();
        //System.out.println(fileDeleted ? "File has been deleted" : "File couldn't be dleeted");
        String deleteMessage = (fileDeleted ? "File has been deleted" : "File couldn't be dleeted");
        log.info(deleteMessage);
    }
}
