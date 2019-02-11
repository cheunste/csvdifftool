package VarexpInterface.Comparison;

import VarexpInterface.Database.*;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
This class is responsible for Importing the config files (PcVue 9, PcVue 11, Matrikon)
into the mysql database. Its
 */
public class ConfigImportTask extends Task<Void> {

    static final Logger logger = LogManager.getLogger(CompareTask.class.getName());
    private static String oldDB;
    private static String newDB;
    private static String matrikonDB;
    private static String oldConfigFilePath;
    private static String newConfigFilePath;
    private static String matrikonFilePath;
    private static final int MAX_TASKS = 4;

    //Constructor. All it does is set up parameters to member variables
    public ConfigImportTask(String oldDB, String newDB, String matrikonDB,
                            String oldFilePath, String newFilePath, String matrikonFilePath
    ) {
        ConfigImportTask.oldDB = oldDB;
        ConfigImportTask.newDB = newDB;
        ConfigImportTask.matrikonDB = matrikonDB;
        ConfigImportTask.oldConfigFilePath = oldFilePath;
        ConfigImportTask.newConfigFilePath = newFilePath;
        ConfigImportTask.matrikonFilePath = matrikonFilePath;
    }

    private static void importFile(String fileLocation, String databaseName) throws IOException, SQLException {

        boolean dbExists = dbConnector.verifyDBExists(databaseName);

        if (dbExists) {

            dbConnector.deleteDB(databaseName);
            dbConnector.createVarexpDB(databaseName);
        }
        importHelper(fileLocation, databaseName);
    }

    //This function imports a MatrikonFactory configuration file
    private static void importMatrikon(String fileLocation, String databaseName) throws IOException, SQLException {

        boolean dbExists = dbConnector.verifyDBExists(databaseName);

        if (dbExists) {
            dbConnector.deleteDB(databaseName);
            dbConnector.createMatrikonDB(databaseName);
        }
        matrikonHelper(fileLocation, databaseName);

    }

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

    //An assistant method to importFile. This Does the actual work of importing the DB Really needs a better name
    private static void matrikonHelper(String fileLocation, String databaseName) throws IOException, SQLException {
        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //This is the consumer. It consumes data in the queue
        Future<Boolean> future = executor.submit(new ImportHandler(buffer, databaseName));
        //This will be the producer (see producer-consumer problem if you're not familiar with hte term)
        executor.execute(new ImportMatrikon(fileLocation, databaseName, buffer));
        //Shtudown
        executor.shutdown();
    }

    @Override
    protected Void call() throws Exception {
        //This is an exector pool. It allows me to submit conccurent tasks while getting a
        // Future for progress update
        ExecutorService importPool = Executors.newSingleThreadExecutor();

        logger.info("Importing the old config file to its DB ");
        Future oldCOnfigFuture = importPool.submit(new ImportThreadExecutor(oldConfigFilePath, oldDB, false));
        logger.info("Importing the new config file to its DB");
        Future newConfigFuture = importPool.submit(new ImportThreadExecutor(newConfigFilePath, newDB, false));
        logger.info("Importing the matrikon file to the DB");
        Future matrikonFuture = importPool.submit(new ImportThreadExecutor(matrikonFilePath, matrikonDB, true));

        importPool.shutdown();

        /*
        Don't do anything until all the imports are done
        In the meantime, if the future gets called back, then call updateProgres. This will
        let the main javafx thread know that some things are done.

        Warning: This is implemented with significant laziness on my part....you'll see what I mean
        */

        int taskTracker = 0;
        logger.info("TaskTracker: " + taskTracker);
        boolean oldFutureDone = false;
        boolean newFutureDone = false;
        boolean matrikonFutureDone = false;
        while (!importPool.isTerminated()) {

            if (oldCOnfigFuture.isDone() && !oldFutureDone) {
                taskTracker++;
                oldFutureDone = true;
                this.updateProgress(taskTracker, MAX_TASKS);
            }

            if (newConfigFuture.isDone() && !newFutureDone) {
                taskTracker++;
                newFutureDone = true;
                this.updateProgress(taskTracker, MAX_TASKS);
            }

            if (matrikonFuture.isDone() && !matrikonFutureDone) {
                taskTracker++;
                matrikonFutureDone = true;
                this.updateProgress(taskTracker, MAX_TASKS);
            }
        }
        taskTracker++;
        this.updateProgress(taskTracker, MAX_TASKS);
        logger.info("TaskTracker: " + taskTracker);
        logger.info("Imported Configs");

        //At this point, return a null. Then in the callback lambda in the main javafx thread
        //Raise an alert or whatever and then execute some other code
        return null;
    }

    public static class ImportThreadExecutor implements Callable<String> {

        String dbName;
        String configFilePath;
        boolean isMatrikon;

        public ImportThreadExecutor(String configfilePath, String dbName, boolean isMatrikon) {
            this.dbName = dbName;
            this.configFilePath = configfilePath;
            this.isMatrikon = isMatrikon;
        }

        @Override
        public String call() throws Exception {
            if (!isMatrikon)
                importFile(this.configFilePath, this.dbName);
            else {
                importMatrikon(this.configFilePath, this.dbName);
            }
            return this.dbName;
        }
    }
}
