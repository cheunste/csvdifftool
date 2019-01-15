package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
This class is responsible for interfacing with the config file config.xml and loading them up into member variables that
can then be used statically

All it does is return the default parameters to other classes that needs them

This Manager class really should be executed first before any other classes
 */
public class PropertyManager {
    //Member variables. These are static so I don't need to freaking instaneate an object before using. That sounds stupid
    private static String user;
    private static String password;
    private static String databaseIP;
    private static String defaultFileName;
    private static String defaultFilePath;
    private static String currentMachine;

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

    public void getPropertyValues() throws IOException {
        try {

            Properties prop = new Properties();
            String propFileName = "comparisonToolConfig.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found");
            }

            //Set to member variables
            user = prop.getProperty("user");
            databaseIP = prop.getProperty("databaseIP");
            password = prop.getProperty("password");
            defaultFileName = prop.getProperty("defaultFileName");
            //defaultFilePath = prop.getProperty("defaultFilePath");

            //Get the path on where the java app was executed
            //defaultFilePath = System.getProperty("user.dir");
            defaultFilePath = prop.getProperty("defaultFilePath");

            //Get the Current Hostname. This is needed as you might not be querying data from localhost
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            currentMachine = localMachine.toString();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }
}
