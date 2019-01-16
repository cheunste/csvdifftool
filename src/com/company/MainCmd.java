package com.company;

import com.company.Comparison.Result;
import com.company.Database.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainCmd {
    private static int FIVE_SECONDS = 5000;

    //Function to import the varexp file. Ask a user if they want to overwrite a DB first and then actually import it
    private static void importFile(String fileLocation, String databaseName) throws IOException, SQLException {

        dbConnector dbc = new dbConnector();

        boolean dbExists = dbc.verifyDBExists(databaseName);

        if (dbExists) {

            //System.out.println("Database already exists. Overwrite? (Y/N)");
            //Scanner sc = new Scanner(System.in);
            //String choice = sc.next().toLowerCase();

            deleteDB(databaseName);
            createVarexpDB(databaseName);
            importHelper(fileLocation, databaseName);

            //if (choice.equals("yes")) {
            //} else {
            //    System.out.println("No change will be made. Exiting tool");
            //}
        } else {
            importHelper(fileLocation, databaseName);
        }
    }

    //An assistant method to importFile. This Does the actual work of importing the DB Really needs a better name
    private static void importHelper(String fileLocation, String databaseName) throws IOException, SQLException {

        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //This is the consumer. It consumes data in the queue
        Future<Boolean> future = executor.submit(new ImportHandler(buffer, databaseName));
        //This will be the producer (see producer-consumer problem if you're not familiar with hte term)
        executor.execute(new Import(fileLocation, databaseName, buffer));
        //Shtudown
        executor.shutdown();
    }

    //This function imports a MatrikonFactory configuration file
    private static void importMatrikon(String fileLocation, String databaseName) throws IOException, SQLException {
        dbConnector dbc = new dbConnector();

        boolean dbExists = dbc.verifyDBExists(databaseName);

        if (dbExists) {
            deleteDB(databaseName);
            dbc.createMatrikonDB(databaseName);
        }
        matrikonHelper(fileLocation, databaseName);
    }

    //An assistant method to importFile. This Does the actual work of importing the DB Really needs a better name
    private static void matrikonHelper(String fileLocation, String databaseName) throws IOException, SQLException {
        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //This is the consumer. It consumes data in the queue
        //Future<Boolean> future = executor.submit(new ImportHandler(buffer, databaseName));
        //This will be the producer (see producer-consumer problem if you're not familiar with hte term)
        executor.execute(new ImportMatrikon(fileLocation, databaseName, buffer));
        //Shtudown
        executor.shutdown();
    }


    private static void deleteDB(String databaseName) {
        dbConnector db = new dbConnector();
        db.deleteDB(databaseName);
        db.close();
    }

    private static void createVarexpDB(String databaseName) {
        dbConnector db = new dbConnector();
        db.createVarexpDB(databaseName);
        db.close();
    }

    //Overloaded method solely used for Matrikon imports
    private static void createMatrikonDB(String databaseName) {
        dbConnector db = new dbConnector();
        db.createMatrikonDB(databaseName);
        db.close();
    }

    private static void createOuptutDB(String databaseName) {

        dbConnector db = new dbConnector();
        db.close();

    }

    public static void main(String[] args) throws IOException, ArrayIndexOutOfBoundsException, SQLException {
        String oldDB = "oldVarexpDB";
        String newDB = "newVarexpDB";
        String matrikonDB = "matrikonDB";

        String resultDB = "resultoutput";

        //Fetch fields from the config file
        PropertyManager pm = new PropertyManager();
        pm.getPropertyValues();

        //Debug Mode. In this mode, everything important should be logged but more importantly, the DB should not be deleted.
        //TODO: When a GUI is created, create a checkbox that allows user to select Debug mode
        boolean debugMode = true;

        //Create a reference to Result class. Result class is used for output. Instaniating it will create
        // the DB used to store results
        Result.createResultDB();

        //Import the Old Varexp and New Varexp into a newVarexpDB and oldVarexpDB and create a finalVarexpDB
        createVarexpDB(oldDB);
        createVarexpDB(newDB);


        createMatrikonDB(matrikonDB);
        System.out.println("DBs created");


        //Read three files in the current directory
        //TODO: Remove the hardcoded file names when you impelment the GUI. Throw this in a config file somewhere

        //String fileDirectory = "C:\\Users\\Stephen\\Documents\\ComparisonTool\\";
        String fileDirectory = PropertyManager.getDefaultFilePath();


        //Import the varexps
        //TODO: Remove the hardcoded file names when you impelment the GUI. Throw this in a config file somewhere
        importFile(fileDirectory + "Varexp_FE03_SHILO_OLD.csv", oldDB);
        importFile(fileDirectory + "Varexp_FE03_SHILO_NEW.csv", newDB);

        //Import the MatrikonFactory file
        //TODO: Remove the hardcoded file names when you impelment the GUI. Throw this in a config file somewhere
        importMatrikon(fileDirectory + "Matrikon_FE03_SHILO.csv", matrikonDB);

        //Get the number of items from the DB. If they do not match, then throw a warning and end the program.
        boolean equalLines = Merge.compareLines(oldDB, newDB, matrikonDB);
        if (!equalLines) {
            System.out.println("Line not equal");
            //End the program
        } else {

        }

        //Create a result Database and a  Result Table.

        //Wait some time in order to let mysql set up all the DB
        dbWait(FIVE_SECONDS);
        Result.executeTests(matrikonDB, newDB, oldDB);

        //Export the database
        Result.exportResult();
        System.out.println(PropertyManager.getDefaultFileName() + " created");

        //Drop the databases (becaues at this point, you're done)
        System.out.println("DBs deleted");

        //Delete the DB
        if (!debugMode) {
            deleteDB(oldDB);
            deleteDB(newDB);
            //deleteDB(resultDB);
            deleteDB(matrikonDB);
        }
    }

    private static void dbWait(int milliseconds) {
        try {
            Thread.sleep(5000);
            //Result.executeTests(matrikonDB, newDB, oldDB);
        } catch (InterruptedException e) {
        }

    }


    public static void exportFile(String databaseName, String outputFilePath) {
        //Shtudown
        Buffer buffer = new Buffer();
        Export exp = new Export(databaseName, buffer, outputFilePath);
        exp.exportDB();
    }

}
