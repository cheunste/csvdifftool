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

    InputStream inputStream;

    //Accessors
    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return getPassword();
    }

    public static String getDatabaseIP() {
        return getDatabaseIP();
    }

    public static String getDefaultFileName() {
        return getDefaultFileName();
    }

    public static String getDefaultFilePath() {
        return getDefaultFilePath();
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
            defaultFilePath = prop.getProperty("defaultFilePath");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }
}
