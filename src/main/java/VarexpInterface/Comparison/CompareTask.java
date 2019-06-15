package VarexpInterface.Comparison;

import VarexpInterface.PropertyManager;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/*

This  class is responsible for starting a single task that executes the
Result class in a separate thread.

I have this class mainly because I can extend the Task class in order to make class to
updateProgress

 */
public class CompareTask extends Task<Void> {


    static final Logger logger = LogManager.getLogger(CompareTask.class.getName());
    private static String oldDB;
    private static String newDB;
    private static String matrikonDB;
    private static String oldConfigFilePath;
    private static String newConfigFilePath;
    private static String matrikonFilePath;
    private static boolean debugMode;
    private static final int MAX_TASK = 6;
    private static int task = 0;


    //Constructor
    public CompareTask(String oldDB, String newDB, String matrikonDB,
                       String oldFilePath, String newFilePath, String matrikonFilePath,
                       boolean debugMode) {

        CompareTask.oldDB = oldDB;
        CompareTask.newDB = newDB;
        CompareTask.matrikonDB = matrikonDB;
        CompareTask.oldConfigFilePath = oldFilePath;
        CompareTask.newConfigFilePath = newFilePath;
        CompareTask.matrikonFilePath = matrikonFilePath;
        CompareTask.debugMode = debugMode;

    }

    @Override
    protected Void call() throws IOException {

        PropertyManager pm = new PropertyManager();
        try {
            pm.getPropertyValues();
            this.updateProgress(task++, MAX_TASK);
        } catch (IOException e) {
            logger.info("Issue with getting the property values");
            throw e;
        }

        try {
            Result.deleteResultDB();
            this.updateProgress(task++, MAX_TASK);
            logger.info("Result DB Deleted");
        } catch (Exception e) {
            logger.info("Error with attempting to delete Result DB: " + e);
        }

        //Create the result DB
        Result.createResultDB();
        this.updateProgress(task++, MAX_TASK);
        exportResult();
        this.updateProgress(task++, MAX_TASK);
        return null;
    }

    private void exportResult() {
        Result.executeTests(matrikonDB, newDB, oldDB);
        this.updateProgress(task++, MAX_TASK);
        Result.exportResult();
        this.updateProgress(task++, MAX_TASK);

        //Reset task
        task = 0;
    }
}
